package com.sinse.universe.domain;

import com.sinse.universe.enums.ProductStatus;
import com.sinse.universe.enums.UserStatus;
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

    @Column(name = "PD_PRICE", nullable = false)
    private Integer price;

    @Column(name = "PD_REGIST_DATE", nullable = false)
    private LocalDateTime registDate;

    @Column(name = "PD_OPEN_DATE")
    private LocalDateTime openDate;

    @Column(name = "PD_FAN_ONLY")
    private Boolean fanOnly = false;

    @Column(name = "PD_STOCK_QTY")
    private Integer stockQuantity = 0;

    @Column(name = "PD_LIMIT_PER_USER")
    private Integer limitPerUser = 0;

    @Column(name = "PD_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CT_ID")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AR_ID")
    private Artist artist;
}
