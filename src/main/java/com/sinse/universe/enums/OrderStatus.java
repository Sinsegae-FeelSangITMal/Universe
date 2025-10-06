package com.sinse.universe.enums;

public enum OrderStatus {
    PENDING("결제 대기"),
    PAID("주문 완료"),       // enum 생성자
    CANCELED("주문 취소"),
    SHIPPING("배송 중"),
    SHIPPED("배송 완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
