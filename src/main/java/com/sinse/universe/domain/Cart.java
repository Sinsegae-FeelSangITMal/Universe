package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="CART")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "CT_ID")
    private int id;
    @Column(name = "CT_QTY", nullable = false)
    private int qty = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PD_ID")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UR_ID")
    private User user;
}
