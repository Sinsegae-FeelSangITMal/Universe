package com.sinse.universe.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;

@Service
@Primary // 이 구현체를 우선적으로 주입
@RequiredArgsConstructor
@Slf4j
public class ObjectStorageService implements StorageService {

    private final S3Client s3;
    private final S3Presigner presigner;

    @Value("${ncp.object-storage.bucket}")
    private String bucket;

    @Value("${upload.base-dir}")
    private String localBaseDir;

    /**
     * 파일을 Object Storage에 업로드 (비공개 업로드가 기본)
     * @param file 업로드 파일
     * @param dir  버킷 내 디렉터리(예: "uploads")
     * @return S3 Key (예: uploads/uuid.png)
     */
    @Override
    public String store(MultipartFile file, String dir) throws IOException {
        // 로컬 파일 시스템 경로를 Object Storage 키로 변환
        String relativeDir = dir;
        if (relativeDir.startsWith(localBaseDir)) {
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
                .acl(ObjectCannedACL.PRIVATE)    // 기본 비공개
                .build();

        s3.putObject(put, RequestBody.fromBytes(file.getBytes()));
        log.info("Uploaded to NCP: s3://{}/{}", bucket, key);

        return key;
    }

    /**
     * 비공개 객체 접근용 프리사인드 URL 발급
     */
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
}
