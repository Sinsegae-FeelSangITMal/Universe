package com.sinse.universe.controller;

import com.sinse.universe.domain.User;
import com.sinse.universe.dto.request.EmailSendRequest;
import com.sinse.universe.dto.request.EmailVerifyRequest;
import com.sinse.universe.dto.request.OAuth2JoinRequest;
import com.sinse.universe.dto.request.BusinessLoginRequest;
import com.sinse.universe.dto.request.UserJoinRequest;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.TokenPair;
import com.sinse.universe.model.auth.AuthServiceImpl;
import com.sinse.universe.model.auth.PartnerUserDetails;
import com.sinse.universe.util.CookieUtil;
import com.sinse.universe.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

/**
 * 로그인, 인증 관련 요청을 처리하는 컨트롤러
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // access token 만료 시 재발급 api
    @PostMapping("/api/auth/accessToken")
    public ResponseEntity<ApiResponse<Object>> reissueToken(
            @CookieValue(name = "refreshToken") String refreshToken
    ) {
        TokenPair tokenPair = authService.reissueTokens(refreshToken);
        ResponseCookie cookie = CookieUtil.setResponseCookie(tokenPair.refreshToken(), jwtUtil.getRefreshTokenTtl());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse<>(true, "새 access token 발급 성공", null, Map.of("accessToken", tokenPair.accessToken())));
    }

    // 로그아웃 시 refreshtoken 제거
    @PostMapping("/api/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refreshToken") String refreshToken
    ) {
        authService.logout(refreshToken);
        ResponseCookie cookie = CookieUtil.setResponseCookie("", Duration.ZERO);

        log.debug("로그아웃 요청");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse<>(true, "로그아웃 성공", null, null));
    }

    // 이메일 인증 코드 발송 api
    @PostMapping("/api/auth/verification-emails")
    public ResponseEntity<ApiResponse<Void>> sendEmailCode(@Valid @RequestBody EmailSendRequest request) {
        authService.sendVerificationCode(request.getEmail());
        return ApiResponse.success("인증 코드가 발송되었습니다. 이메일을 확인해주세요.");
    }

    @PostMapping("/api/auth/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@Valid @RequestBody EmailVerifyRequest request) {
        authService.verifyEmail(request.getEmail(), request.getInputCode());
        return ApiResponse.success("인증되었습니다. 회원가입을 진행해 주세요.");
    }

    @PostMapping("/api/ent/auth/join")
    public ResponseEntity<ApiResponse<Void>> join(@RequestBody @Valid UserJoinRequest request) {
        authService.join(request);
        return ApiResponse.success("회원가입이 완료되었습니다. 다시 로그인 해주세요.");
    }

    @PostMapping("/api/auth/oauth2/join")
    public ResponseEntity<ApiResponse<Object>> joinOAuthUser(@RequestBody @Valid OAuth2JoinRequest request) {
        TokenPair tokenPair = authService.joinOAuthUser(request);
        ResponseCookie cookie = CookieUtil.setResponseCookie(tokenPair.refreshToken(), jwtUtil.getRefreshTokenTtl());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse<>(true, "회원가입이 완료되었습니다.", null, Map.of("accessToken", tokenPair.accessToken())));
    }

    @PostMapping("/api/ent/auth/login")
    public ResponseEntity<ApiResponse<Object>> loginBusinessUser(@RequestBody @Valid BusinessLoginRequest request) {
        log.debug("auth controller 진입");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        PartnerUserDetails userDetails = (PartnerUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getRole().getName().name(), user.getEmail(), user.getName());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        ResponseCookie cookie = CookieUtil.setResponseCookie(refreshToken, jwtUtil.getRefreshTokenTtl());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse<>(true, "로그인 성공", null, Map.of("accessToken", accessToken)));
    }
}
