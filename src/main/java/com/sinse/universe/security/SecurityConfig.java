package com.sinse.universe.security;

import com.sinse.universe.filter.JwtAuthFilter;
import com.sinse.universe.model.auth.OAuth2FailureHandler;
import com.sinse.universe.model.auth.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())   // WebConfig의 Cors 설정이 적용되도록함
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //로그인 관련 (OAuth2, 토큰 재발급 등)
                        .requestMatchers("/oauth2/**", "/api/auth/**").permitAll()
                        //.requestMatchers().permitAll() //API 요청 중 인증이 필요없는 공개 api

                       // .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()   // 개발할때만 잠깐 열어둘 용도로 모든 요청 허용

//                        .anyRequest().authenticated()
               )
                //스프링이 자동으로 "로그인 요청 -> 리다이렉트 -> 토큰 교환 -> 사용자 정보 조회" 과정을 처리해줌
                .oauth2Login(oauth2->oauth2    //oauth2 로그인 기능 활성화
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                // 로그인 안돼있는 경우 구글로그인 리다이렉트되는 것 막기
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, ex1) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"code\":\"UNAUTHORIZED\",\"message\":\"로그인이 필요합니다.\"}");
                        })
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}