package com.sinse.universe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "STREAM_PRODUCT")
@Data
public class StreamProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SR_PD_ID")
    private int id;

    // Stream N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SR_ID", nullable = false)
    @JsonIgnore
    private Stream stream;

    // Product N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PD_ID", nullable = false)
    @JsonIgnore
    private Product product;
}