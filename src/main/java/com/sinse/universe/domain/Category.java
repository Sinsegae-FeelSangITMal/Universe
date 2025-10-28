package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="CATEGORY")
@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CT_ID")
    private int id;

    @Column(name="CT_NAME")
    private String name;
}
