package com.sinse.universe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinse.universe.domain.Order;
import com.sinse.universe.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderForEntResponse(
        String no,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime date,
        OrderStatus status,
        Integer totalPrice,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime cancelDate,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime refundDate,
        String userName,
        List<OrderProductForEntResponse> orderProducts
        ) {
    public static OrderForEntResponse from(Order o) {
        return new OrderForEntResponse(
                o.getNo(),
                o.getDate(),
                o.getStatus(),
                o.getTotalPrice(),
                o.getCancelDate(),
                o.getRefundDate(),
                o.getUser().getName(),
                o.getOrderProducts().stream()
                    .map(OrderProductForEntResponse::from)
                    .toList()
        );
    }
}