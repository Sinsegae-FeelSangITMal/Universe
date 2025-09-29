package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="ORDER_PROMOTION")
@Data
public class OrderPromotion {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "OR_PM_ID")
    private int id;
    @Column(name = "OR_PD_QTY", nullable = false)
    private int qty = 1;
    @Column(name="OR_PM_DATE")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OR_ID", nullable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PM_ID", nullable = false)
    private Promotion promotion;
}
