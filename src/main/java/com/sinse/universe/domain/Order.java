package com.sinse.universe.domain;

import com.sinse.universe.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ORDERS")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "OR_ID")
    private int id;

    @Column(name = "OR_NO", nullable = false, unique = true)
    private String no;

    @Column(name = "OR_DATE", nullable = false, updatable = false)
    private LocalDateTime date;

    @Column(name = "OR_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "OR_TOTAL_PRICE", nullable = false)
    private int totalPrice;

    @Column(name = "OR_PAYM_METD", nullable = false)
    private String payment;

    // 주문자 정보 (스냅샷)
    @Column(name = "OR_ORDERER_NAME")
    private String ordererName;

    @Column(name = "OR_ORDERER_EMAIL")
    private String ordererEmail;

    // 수령인 정보
    @Column(name = "OR_RECEIVER_NAME")
    private String receiverName;

    @Column(name = "OR_RECEIVER_PHONE")
    private String receiverPhone;

    @Column(name = "OR_RECEIVER_ADDR")
    private String receiverAddr;

    @Column(name = "OR_RECEIVER_ADDR_DETAIL")
    private String receiverAddrDetail;

    @Column(name = "OR_RECEIVER_COUNTRY")
    private String receiverCountry;

    @Column(name = "OR_RECEIVER_CITY")
    private String receiverCity;

    @Column(name = "OR_RECEIVER_STATE")
    private String receiverState;

    @Column(name = "OR_RECEIVER_POSTAL")
    private String receiverPostal;

    // 약관/결제 동의 여부
    @Column(name = "OR_AGREE", nullable = false)
    private boolean agree;

    @Column(name = "OR_CANCEL_DATE")
    private LocalDateTime cancelDate;

    @Column(name = "OR_REFUND_DATE")
    private LocalDateTime refundDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UR_ID", nullable = false)
    private User user;

    // OrderItem 매핑하기
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrderProduct> orderProducts = new ArrayList<>();
}
