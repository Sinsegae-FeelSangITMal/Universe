package com.sinse.universe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "PARTNER")
@Data
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PT_ID")
    private int id;

    @Column(name = "PT_NAME")
    private String name;

    @Column(name = "PT_ADR")
    private String address;

    @Column(name = "PT_FIL")
    private String file;

    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY)
    @JsonIgnore  // ✅ 순환참조 방지
    private List<Artist> artists;
}

