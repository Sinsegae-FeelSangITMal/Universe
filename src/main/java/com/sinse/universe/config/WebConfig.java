package com.sinse.universe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.stream.Stream;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @Value("${upload.base-dir}")
    private String baseDir;

    @Value("${app.front-server.user.url}")
    private String frontUserServerUrl;

    @Value("${app.front-server.partner.url}")
    private String frontPartnerServerUrl;

    @Value("${app.front-server.admin.url}")
    private String frontAdminServerUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(urlPrefix + "/**")
                .addResourceLocations("file:///" + baseDir + "/");
    }

    // ✅ @Bean 제거 — WebMvcConfigurer의 기본 메서드로 바로 정의
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 쉼표로 구분된 다중 Origin 처리
        String[] adminOrigins = frontAdminServerUrl.split(",");

        // 모든 origin 합치기
        String[] allowedOrigins = Stream.concat(
                Stream.of(frontUserServerUrl, frontPartnerServerUrl),
                Arrays.stream(adminOrigins)
        ).toArray(String[]::new);

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
