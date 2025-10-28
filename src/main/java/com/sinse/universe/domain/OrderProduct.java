package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="ORDER_PRODUCT")
@Data
public class OrderProduct {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "OR_PD_ID")
    private int id;
    @Column(name = "OR_PD_QTY", nullable = false)
    private int qty = 1;
    @Column(name = "OR_PD_PRICE", nullable = false)
    private int price;
    @Column(name = "OR_PD_TOTAL", insertable = false, updatable = false)
    private int total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OR_ID", nullable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PD_ID", nullable = false)
    private Product product;
}
