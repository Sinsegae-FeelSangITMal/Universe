package com.sinse.universe.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/*
    Spring MVC 기반 - CORS 정책 위반 에러에 대한 처리

    이후 Gateway 추가시 이 코드 불필요
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/upload/");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 요청 경로 허용
                .allowedOrigins("http://localhost:5555")
                    .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS") // PUT요청시 OPTIONS 필요 -하지만 Security가 있으면 적용 안 될 가능성이 큽니다-
                    .allowedHeaders("*")
                    .allowCredentials(true); // 인증정보(쿠키) 포함 허용 여부
            }
        };
    }
    
}
