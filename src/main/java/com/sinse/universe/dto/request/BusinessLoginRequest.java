package com.sinse.universe.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BusinessLoginRequest(
        @NotBlank(message = "이메일을 입력해주세요")
        @Email
        String email,
        @NotBlank(message = "비밀번호를 입력해주세요")
        String password
) {
}
