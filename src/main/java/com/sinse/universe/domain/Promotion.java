package com.sinse.universe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROMOTION")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PM_ID")
    private int id;

    @Column(name = "PM_NAME", nullable = false, length = 200)
    private String name;

    @Column(name = "PM_DESC", columnDefinition = "TEXT")
    private String description;

    @Column(name = "PM_IMG", length = 255)
    private String img;

    @Column(name = "PM_PRICE", nullable = false)
    private int price;

    @Column(name = "PM_FAN_ONLY", nullable = false)
    private boolean fanOnly = false;

    @Column(name = "PM_STOCK_QTY", nullable = false)
    private int stockQty = 0;

    @Column(name = "PM_LIMIT_PER_USER", nullable = false)
    private int limitPerUser = 1;

    @Column(name = "PM_COUPON", length = 200)
    private String coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AR_ID", nullable = false)
    @JsonIgnore
    private Artist artist;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Stream> streams = new ArrayList<>();
}