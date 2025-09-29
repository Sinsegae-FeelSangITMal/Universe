package com.sinse.universe.dto.response;

import com.sinse.universe.domain.User;

public record OrderUserResponse(
        Integer id,
        String name
        ) {
    public static OrderUserResponse from(User u) {
        return new OrderUserResponse(
                u.getId(),
                u.getName()
        );
    }
}