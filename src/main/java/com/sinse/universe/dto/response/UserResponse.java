package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Order;
import com.sinse.universe.domain.User;
import com.sinse.universe.enums.RoleName;
import com.sinse.universe.enums.UserStatus;

import java.time.LocalDateTime;

public record UserResponse(
        Integer id,
        String loginId,
        String name,
        UserStatus status,
        LocalDateTime joinDate
        //RoleName roleName
        ) {
    public static UserResponse from(User u) {
        return new UserResponse(
                u.getId(),
                u.getLoginId(),
                u.getName(),
                u.getStatus(),
                u.getJoinDate()//,
                //u.getRole() != null ? u.getRole().getName() : null
        );
    }
}