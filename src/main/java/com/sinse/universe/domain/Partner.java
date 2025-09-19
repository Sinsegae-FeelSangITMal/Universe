package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PT_ID")
    private int id;

    @Column(name = "PT_NM", nullable = false)
    private String name;

    @Column(name = "PT_ADR", nullable = false)
    private String address;

    @Column(name = "PT_FIL", nullable = false)
    private String filePath;
}


