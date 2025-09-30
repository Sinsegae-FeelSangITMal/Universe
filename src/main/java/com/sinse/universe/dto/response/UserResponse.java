package com.sinse.universe.dto.response;


import com.sinse.universe.domain.Cart;
import com.sinse.universe.domain.User;

public record UserResponse(
        int userId,
        String nickname,
        String roleName,
        String email
) {
    public static UserResponse from(User u) {
        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getRole().getName().name(),
                u.getEmail()
        );
    }
}
