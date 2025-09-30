package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Cart;
import com.sinse.universe.domain.Order;
import com.sinse.universe.enums.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record CartResponse(
        Integer id,
        Integer qty,
        CartProductResponse product,
        Integer userId
        ) {
    public static CartResponse from(Cart c) {
        return new CartResponse(
                c.getId(),
                c.getQty(),
                CartProductResponse.from(c.getProduct()),
                c.getUser().getId()
        );
    }
}