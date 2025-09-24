package com.sinse.universe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinse.universe.domain.Role;
import com.sinse.universe.domain.User;
import com.sinse.universe.enums.UserRole;
import com.sinse.universe.enums.UserStatus;

import java.time.LocalDateTime;

/**
 * 사이트 관리자 - 유저 목록 조회
 */
public record UserListForAdminResponse(
        int id,
        String loginId,
        String nickname,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime regdate,
        UserRole role,
        UserStatus status
){
        // Entity -> DTO
        public static UserListForAdminResponse from(User u) {
                return new UserListForAdminResponse(
                        u.getId(),
                        u.getLoginId(),
                        u.getName(),
                        u.getJoinDate(),
                        u.getRole().getName(),
                        u.getStatus()
                );
        }
}
