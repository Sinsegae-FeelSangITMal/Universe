package com.sinse.universe.model.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.util.CookieUtil;
import com.sinse.universe.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // oauth provider가 전달한 유저 정보가 Authentication에 담긴 상태(구현체인 OAuth2AuthenticationToken)

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();  // CustomOAuth2UserService에서 반환해준 객체

        // JWT 토큰 발급
        int userId = oAuth2User.getUser().getId();
        String roleName = oAuth2User.getUser().getRole().getName().name();
        String accessToken = jwtUtil.createAccessToken(userId, roleName);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        ResponseCookie refreshCookie = CookieUtil.getResponseCookie(refreshToken, jwtUtil.getRefreshTokenTtl());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                .success(true)
                .message("로그인에 성공했습니다.") // 사용자 메시지
                .code("SUCCESS")                // 시스템 코드
                .data(Map.of("accessToken", accessToken))
                .build();

        // 컨트롤러 메서드 진입 전에 핸들러가 호출되기 때문에 response body에 직접 write 해야함
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset-utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
