package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="PARTNER")
@Entity
@Data
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PT_ID")
    private int id;

    @Column(name="PT_NM")
    private String name;

    @Column(name="PT_ADR")
    private String address;

    @Column(name="PT_FIL")
    private String file;
}
