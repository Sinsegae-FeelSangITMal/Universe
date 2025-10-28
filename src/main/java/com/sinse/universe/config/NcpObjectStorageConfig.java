package com.sinse.universe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.util.Objects;

@Configuration
public class NcpObjectStorageConfig {

    @Bean
    public S3Client s3Client(Environment env) {
        String endpoint = Objects.requireNonNull(env.getProperty("ncp.object-storage.endpoint"));
        String region   = Objects.requireNonNull(env.getProperty("ncp.object-storage.region"));
        String accessKey = Objects.requireNonNull(env.getProperty("ncp.object-storage.access-key"));
        String secretKey = Objects.requireNonNull(env.getProperty("ncp.object-storage.secret-key"));

        AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))                 // NCP: kr-standard
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .endpointOverride(URI.create(endpoint))    // NCP 전용 엔드포인트 중요!
                .build();
    }

    @Bean
    public S3Presigner s3Presigner(Environment env) {
        String endpoint = Objects.requireNonNull(env.getProperty("ncp.object-storage.endpoint"));
        String region   = Objects.requireNonNull(env.getProperty("ncp.object-storage.region"));

        String accessKey = Objects.requireNonNull(env.getProperty("ncp.object-storage.access-key"));
        String secretKey = Objects.requireNonNull(env.getProperty("ncp.object-storage.secret-key"));

        AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .endpointOverride(URI.create(endpoint))
                .build();
    }
}