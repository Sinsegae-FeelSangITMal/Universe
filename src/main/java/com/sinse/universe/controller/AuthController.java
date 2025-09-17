package com.sinse.universe.controller;

import com.sinse.universe.dto.request.EmailSendRequest;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.model.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *  사이트 전역적 권한 관련 요청을 처리하는 컨트롤러
 */

@RestController
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    // 이메일 인증 코드 발송 api
    @PostMapping("/api/auth/send/code")
    public ResponseEntity<ApiResponse<Object>> sendEmailCode(@Valid @RequestBody EmailSendRequest request) {
        authService.sendVerificationCode(request.getEmail());
        return ApiResponse.success("인증 코드가 발송되었습니다. 이메일을 확인해주세요.");
    }
}
