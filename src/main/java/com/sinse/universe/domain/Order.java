package com.sinse.universe.domain;

import com.sinse.universe.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

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
    @Column(name = "OR_SHIP_ADDR")
    private String address;
    @Column(name = "OR_RECEIVER")
    private String receiver;
    @Column(name = "OR_PHONE")
    private String phone;
    @Column(name = "OR_CANCEL_DATE")
    private LocalDateTime cancelDate;
    @Column(name = "OR_REFUND_DATE")
    private LocalDateTime refundDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UR_ID", nullable = false)
    private User user;

    // OrderItem 매핑하기 (부분환불/부분취소 같은 기능을 넣을 때만 orderproductrepository를 만들어서 개별 처리)
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts =  new ArrayList<>();
}
