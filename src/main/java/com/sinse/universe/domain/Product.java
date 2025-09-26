package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUCT")
@Data
public class Product {

    public enum ProductStatus { active, inactive } // DB 값과 동일 소문자

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

    @Column(name = "PD_REGIST_DATE")
    private LocalDateTime registDate;

    @Column(name = "PD_OPEN_DATE")
    private LocalDateTime openDate;

    @Column(name = "PD_FAN_ONLY")
    private Boolean fanOnly = false;

    @Column(name = "PD_STOCK_QTY")
    private Integer stockQuantity = 0;

    @Column(name = "PD_LIMIT_PER_USER")
    private Integer limitPerUser = 0;

    @Enumerated(EnumType.STRING)
    @Column(name="pd_status")
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CT_ID")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AR_ID")
    private Artist artist;

    @OneToMany(mappedBy ="product", fetch = FetchType.EAGER)
    private List<ProductImage> productImageList = new ArrayList<>();
}
