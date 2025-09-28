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

    @Value("${app.front-server.user.url}")
    private String frontUserServerUrl;

    @Value("${app.front-server.partner.url}")
    private String frontPartnerServerUrl;

    @Value("${app.front-server.admin.url}")
    private String frontAdminServerUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 예: /uploads/** → file:///C:/upload/
        registry.addResourceHandler(urlPrefix + "/**")
                .addResourceLocations("file:///" + baseDir + "/");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 요청 경로 허용
                        .allowedOrigins(frontUserServerUrl, frontPartnerServerUrl, frontAdminServerUrl) // 프론트 서버들
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")   // 헤더에 accessToken 정보가 담겨오기 때문에 허용
                        .allowCredentials(true); // 인증정보(쿠키) 포함 허용 여부
            }
        };
    }
}