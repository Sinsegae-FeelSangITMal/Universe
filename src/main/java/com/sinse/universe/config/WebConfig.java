package com.sinse.universe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
    Spring MVC 기반 - CORS 정책 위반 에러에 대한 처리

    이후 Gateway 추가시 이 코드 불필요
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 요청 경로 허용
                .allowedOrigins("http://localhost:5174")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowCredentials(true); // 인증정보(쿠키) 포함 허용 여부
            }
        };
    }
    
}
