package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Order;
import com.sinse.universe.domain.User;
import com.sinse.universe.enums.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record OrderForUserResponse(
        String no,
        String date,
        OrderStatus status,
        Integer totalPrice,
        String payment,
        String address,
        String receiver,
        String phone,
        String cancelDate,
        String refundDate,
        UserResponse user,
        List<OrderProductResponse> orderProducts
        ) {
    public static OrderForUserResponse from(Order o) {
        return new OrderForUserResponse(
                o.getNo(),
                formatDate(o.getDate()),
                o.getStatus(),
                o.getTotalPrice(),
                o.getPayment(),
                o.getAddress(),
                o.getReceiver(),
                o.getPhone(),
                formatDate(o.getCancelDate()),
                formatDate(o.getRefundDate()),
                UserResponse.from(o.getUser()),
                o.getOrderProducts().stream()
                    .map(OrderProductResponse::from)
                    .toList()
        );
    }

    private static String formatDate(LocalDateTime dateTime) {
        return dateTime != null
                ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "-";
    }
}