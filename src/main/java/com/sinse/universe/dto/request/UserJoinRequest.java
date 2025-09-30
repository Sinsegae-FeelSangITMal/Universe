package com.sinse.universe.dto.request;

import com.sinse.universe.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// record : 불변성 - 컴파일러 차원에서 강제 -> @Setter 추가 막음
// @Getter @AllArgsConstructor @EqualsAndHashCode @ToString 기본 제공
public record UserJoinRequest(
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        String email,

        // 보안 요구사항 있을 시 추가
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
        String password,

        @NotBlank(message = "비밀번호 확인을 입력해주세요.")
        String passwordConfirm,

        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        UserRole role
){}

