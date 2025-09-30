package com.sinse.universe.controller;

import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.UserResponse;
import com.sinse.universe.model.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/api/user/me")
    public ResponseEntity<ApiResponse<UserResponse>> getLoginUserInfo(@AuthenticationPrincipal int userId) {
        log.debug("/api/user/me request userId={}", userId);
        UserResponse userResponse = userServiceImpl.getUserInfo(userId);
        return ApiResponse.success("/api/user/me, 유저 정보 반환", userResponse);
    }
}
