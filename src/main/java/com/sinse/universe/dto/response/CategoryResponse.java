package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Category;

// 가볍고 안전한 응답 전용 DTO
public record CategoryResponse(
        Integer id,
        String categoryName
) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getName()
        );
    }
}
