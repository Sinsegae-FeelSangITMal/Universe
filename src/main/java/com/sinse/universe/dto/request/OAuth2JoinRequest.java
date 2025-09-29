package com.sinse.universe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OAuth2JoinRequest(
        @NotBlank
        String provider,
        @NotBlank
        String oauthId,
        @NotBlank(message = "닉네임을 입력해주세요")
        @Size(min=2, max=20)
        String nickname
) {
}
