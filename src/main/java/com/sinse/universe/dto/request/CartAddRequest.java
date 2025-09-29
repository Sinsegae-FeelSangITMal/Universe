package com.sinse.universe.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartAddRequest(
    @NotNull
    Integer userId,                 // 유저 아이디
    @NotNull
    Integer productId,            // 상품 아이디
    @NotNull
    Integer qty                     // 상품 수량
) {}
