package com.sinse.universe.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sinse.universe.domain.Order;
import com.sinse.universe.domain.OrderProduct;
import com.sinse.universe.domain.Product;
import com.sinse.universe.enums.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record OrderResponse(
        String no,
        String date,
        OrderStatus status,
        Integer totalPrice,
        String cancelDate,
        String refundDate,
        String userName,
        List<OrderProductResponse> orderProducts
        ) {
    public static OrderResponse from(Order o) {
        return new OrderResponse(
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