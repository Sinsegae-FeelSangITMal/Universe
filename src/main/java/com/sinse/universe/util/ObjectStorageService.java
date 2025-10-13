package com.sinse.universe.util;

import com.sinse.universe.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class ObjectStorageService implements StorageService {

    private final S3Client s3;
    private final S3Presigner presigner;

    @Value("${ncp.object-storage.bucket}")
    private String bucket;

    @Value("${upload.base-dir}")
    private String localBaseDir;

    @Override
    public String store(MultipartFile file, String dir) throws IOException {
        String relativeDir = dir;
        if (relativeDir != null && relativeDir.startsWith(localBaseDir)) {
            relativeDir = relativeDir.substring(localBaseDir.length());
            if (relativeDir.startsWith("/") || relativeDir.startsWith("\\")) {
                relativeDir = relativeDir.substring(1);
            }
        }

        String ext = Optional.ofNullable(file.getOriginalFilename())
                .filter(fn -> fn.contains("."))
                .map(fn -> fn.substring(fn.lastIndexOf('.')))
                .orElse("");

        String key = (relativeDir == null || relativeDir.isBlank() ? "uploads" : relativeDir)
                + "/" + UUID.randomUUID() + ext;

        String contentType = Optional.ofNullable(file.getContentType())
                .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .acl(ObjectCannedACL.PRIVATE)
                .build();

        s3.putObject(put, RequestBody.fromBytes(file.getBytes()));
        log.info("Uploaded to NCP: s3://{}/{}", bucket, key);

        return key; // 예: recording/s68/rec_xxx.webm
    }

    public String getPresignedGetUrl(String key, Duration expire) {
        GetObjectRequest get = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presign = GetObjectPresignRequest.builder()
                .signatureDuration(expire)
                .getObjectRequest(get)
                .build();

        PresignedGetObjectRequest p = presigner.presignGetObject(presign);
        return p.url().toString();
    }

    @Override
    public void deleteObject(String key) {
        if (key == null || key.isBlank()) return;
        try {
            s3.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
            log.info("Deleted from NCP: s3://{}/{}", bucket, key);
        } catch (Exception e) {
            log.warn("Failed to delete s3://{}/{}: {}", bucket, key, e.getMessage());
        }
    }

    @Override
    public void deleteFolderPrefix(String prefix) {
        try {
            var list = s3.listObjectsV2(ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .prefix(prefix.endsWith("/") ? prefix : prefix + "/")
                    .build());

            if (list.hasContents() && !list.contents().isEmpty()) {
                var toDelete = list.contents().stream()
                        .map(o -> ObjectIdentifier.builder().key(o.key()).build())
                        .toList();

                s3.deleteObjects(DeleteObjectsRequest.builder()
                        .bucket(bucket)
                        .delete(d -> d.objects(toDelete))
                        .build());
                log.info("Deleted prefix from NCP: s3://{}/{}", bucket, prefix);
            }
        } catch (Exception e) {
            log.warn("Failed to delete prefix s3://{}/{}: {}", bucket, prefix, e.getMessage());
        }
    }

    /** ✅ S3 객체를 읽기 전용 스트림으로 반환 */
    @Override
    public InputStream getObjectStream(String key) {
        if (key == null || key.isBlank()) {
            throw new com.sinse.universe.exception.CustomException(
                    ErrorCode.INVALID_PARAMETER
            );
        }
        try {
            ResponseInputStream<GetObjectResponse> in = s3.getObject(
                    GetObjectRequest.builder().bucket(bucket).key(key).build()
            );
            return in; // 호출 측에서 닫기 처리
        } catch (S3Exception e) {
            log.warn("S3 getObject failed for s3://{}/{} - {}", bucket, key, e.awsErrorDetails().errorMessage());
            // 존재하지 않거나 권한 문제 등은 404/403으로 매핑되도록 컨트롤러에서 처리
            throw e;
        }
    }
}
