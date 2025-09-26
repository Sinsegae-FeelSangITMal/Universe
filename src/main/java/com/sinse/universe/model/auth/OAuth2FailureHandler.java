package com.sinse.universe.model.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.universe.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 로그인 실패: {}", exception.getMessage());

        // 프론트에 내려줄 응답 구성
        ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                .success(false)
                .message("OAuth 로그인에 실패했습니다. 다시 시도해주세요.") // 사용자 메시지
                .code("OAUTH2_AUTHENTICATION_FAILED")                // 시스템 코드
                .data(null)
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        log.debug("fail response={}", objectMapper.writeValueAsString(apiResponse));
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
