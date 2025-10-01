package com.sinse.universe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinse.universe.domain.Order;
import com.sinse.universe.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderForUserResponse(
        String no,
        String ordererName,
        String ordererEmail,
        String userName, // Users 테이블에서 가져온 회원 이름
        OrderStatus status,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime date,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime cancelDate,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime refundDate,
        String receiverName,
        String receiverPhone,
        Integer totalPrice,
        String payment,
        String receiverCountry,
        String receiverCity,
        String receiverState,
        String receiverPostal,
        String receiverAddr,
        String receiverAddrDetail,
        boolean agree,
        List<OrderProductForUserResponse> orderProducts
) {
    // 기본 버전 (목록용)
    public static OrderForUserResponse from(Order o) {
        return new OrderForUserResponse(
                o.getNo(),
                o.getOrdererName(),
                o.getOrdererEmail(),
                o.getUser().getName(),
                o.getStatus(),
                o.getDate(),
                o.getCancelDate(),
                o.getRefundDate(),
                o.getReceiverName(),
                o.getReceiverPhone(),
                o.getTotalPrice(),
                o.getPayment(),
                o.getReceiverCountry(),
                o.getReceiverCity(),
                o.getReceiverState(),
                o.getReceiverPostal(),
                o.getReceiverAddr(),
                o.getReceiverAddrDetail(),
                o.isAgree(),
                o.getOrderProducts().stream()
                        .map(OrderProductForUserResponse::from) // membership 없음
                        .toList()
        );
    }

    // 상세 조회 버전 (membership-aware)
    public static OrderForUserResponse from(Order o, List<OrderProductForUserResponse> products) {
        return new OrderForUserResponse(
                o.getNo(),
                o.getOrdererName(),
                o.getOrdererEmail(),
                o.getUser().getName(),
                o.getStatus(),
                o.getDate(),
                o.getCancelDate(),
                o.getRefundDate(),
                o.getReceiverName(),
                o.getReceiverPhone(),
                o.getTotalPrice(),
                o.getPayment(),
                o.getReceiverCountry(),
                o.getReceiverCity(),
                o.getReceiverState(),
                o.getReceiverPostal(),
                o.getReceiverAddr(),
                o.getReceiverAddrDetail(),
                o.isAgree(),
                products
        );
    }

}
