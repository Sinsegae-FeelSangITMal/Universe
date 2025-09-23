package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Order;
import com.sinse.universe.domain.OrderProduct;

import java.time.LocalDateTime;

public record OrderProductResponse(
        Integer qty,
        String name
        ) {
    public static OrderProductResponse from(OrderProduct op) {
        return new OrderProductResponse(
                op.getId(),
                op.getProduct().getName()
        );
    }
}