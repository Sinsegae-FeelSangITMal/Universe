package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Order;
import com.sinse.universe.enums.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record OrderForEntResponse(
        String no,
        String date,
        OrderStatus status,
        Integer totalPrice,
        String cancelDate,
        String refundDate,
        String userName,
        List<OrderProductResponse> orderProducts
        ) {
    public static OrderForEntResponse from(Order o) {
        return new OrderForEntResponse(
                o.getNo(),
                formatDate(o.getDate()),
                o.getStatus(),
                o.getTotalPrice(),
                formatDate(o.getCancelDate()),
                formatDate(o.getRefundDate()),
                o.getUser().getName(),
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