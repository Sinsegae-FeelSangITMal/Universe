package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PD_ID")
    private int id;

    @Column(name = "PD_NAME", nullable = false, length = 200)
    private String name;

    @Column(name = "PD_DESC")
    private String description;

    @Column(name = "PD_MAIN_IMG")
    private String mainImage;

    @Column(name = "PD_DETAIL_IMG")
    private String detailImage;

    @Column(name = "PD_PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "PD_OPEN_DATE")
    private LocalDateTime openDate;

    @Column(name = "PD_FAN_ONLY")
    private Boolean fanOnly = false;

    @Column(name = "PD_STOCK_QTY")
    private Integer stockQuantity = 0;

    @Column(name = "PD_LIMIT_PER_USER")
    private Integer limitPerUser = 0;

    @Column(name = "PD_ACTIVE_YN")
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CT_ID")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AR_ID")
    private Artist artist;
}
