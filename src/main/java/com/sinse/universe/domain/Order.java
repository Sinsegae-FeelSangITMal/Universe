package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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

    @Column(name = "OR_STAT")
    private String status;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UR_ID", nullable = false)
    private User user;
}
