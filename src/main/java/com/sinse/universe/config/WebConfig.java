package com.sinse.universe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
    Spring MVC 기반 - CORS 정책 위반 에러에 대한 처리

    이후 Gateway 추가시 이 코드 불필요
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @Value("${upload.base-dir}")
    private String baseDir;

    @Value("${upload.recording-url}")
    private String recordingUrl;

    @Value("${upload.recording-dir}")
    private String recordingDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 이미지 매핑
        registry.addResourceHandler(urlPrefix + "/**")
                .addResourceLocations("file:///" + baseDir + "/");

        // 녹화 영상 매핑
        registry.addResourceHandler(recordingUrl + "/**")
                .addResourceLocations("file:///" + recordingDir + "/");
    }
}