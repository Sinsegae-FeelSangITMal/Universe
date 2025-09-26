package com.sinse.universe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinse.universe.domain.Order;
import com.sinse.universe.domain.User;
import com.sinse.universe.enums.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record OrderForUserResponse(
        String no,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime date,
        OrderStatus status,
        Integer totalPrice,
        String payment,
        String address,
        String receiver,
        String phone,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime cancelDate,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime refundDate,
        UserResponse user,
        List<OrderProductResponse> orderProducts
        ) {
    public static OrderForUserResponse from(Order o) {
        return new OrderForUserResponse(
                o.getNo(),
                o.getDate(),
                o.getStatus(),
                o.getTotalPrice(),
                o.getPayment(),
                o.getAddress(),
                o.getReceiver(),
                o.getPhone(),
                o.getCancelDate(),
                o.getRefundDate(),
                UserResponse.from(o.getUser()),
                o.getOrderProducts().stream()
                    .map(OrderProductResponse::from)
                    .toList()
        );
    }
}