package com.sinse.universe.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
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

        return key;
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

    /** ✅ 클라우드 객체 삭제 */
    public void deleteObject(String key) {
        if (key == null || key.isBlank()) return;
        try {
            s3.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
            log.info("Deleted from NCP: s3://{}/{}", bucket, key);
        } catch (Exception e) {
            // 치명적 실패는 아님 — 로그만
            log.warn("Failed to delete s3://{}/{}: {}", bucket, key, e.getMessage());
        }
    }

    /**  클라우드 폴더 삭제 */
    public void deleteFolderPrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) return;
        try {
            String token = null;
            do {
                ListObjectsV2Response list = s3.listObjectsV2(ListObjectsV2Request.builder()
                        .bucket(bucket)
                        .prefix(prefix.endsWith("/") ? prefix : prefix + "/")
                        .continuationToken(token)
                        .build());
                list.contents().forEach(obj -> deleteObject(obj.key()));
                token = list.nextContinuationToken();
            } while (token != null);
            log.info("Deleted all under prefix: s3://{}/{}", bucket, prefix);
        } catch (Exception e) {
            log.warn("Failed to delete prefix s3://{}/{} : {}", bucket, prefix, e.getMessage());
        }
    }
}
