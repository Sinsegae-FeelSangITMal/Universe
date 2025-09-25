package com.sinse.universe.controller;

import com.sinse.universe.dto.request.EmailSendRequest;
import com.sinse.universe.dto.request.EmailVerifyRequest;
import com.sinse.universe.dto.request.UserJoinRequest;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.model.auth.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 로그인, 인증 관련 요청을 처리하는 컨트롤러
 */

@Slf4j
@RestController
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
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
}
