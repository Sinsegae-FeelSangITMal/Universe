package com.sinse.universe.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartUpdateRequest(
    @NotNull
    Integer qty                     // 상품 수량
) {}
