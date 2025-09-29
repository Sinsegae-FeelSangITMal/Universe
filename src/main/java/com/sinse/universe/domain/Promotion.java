package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PRODUCT")
@Data
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PM_ID")
    private int id;

    @Column(name = "PM_NAME", nullable = false, length = 200)
    private String name;

    @Column(name = "PM_DESC")
    private String description;

    @Column(name = "PM_MAIN_IMG")
    private String mainImage;

    @Column(name = "PM_DETAIL_IMG")
    private String detailImage;

    @Column(name = "PM_PRICE", nullable = false)
    private Integer price;

    @Column(name = "PM_FAN_ONLY")
    private Boolean fanOnly = false;

    @Column(name = "PM_STOCK_QTY")
    private Integer stockQuantity = 0;

    @Column(name = "PM_LIMIT_PER_USER")
    private Integer limitPerUser = 0;

    @Column(name = "PM_COUPON", nullable = false)
    private String coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AR_ID")
    private Artist artist;
}
