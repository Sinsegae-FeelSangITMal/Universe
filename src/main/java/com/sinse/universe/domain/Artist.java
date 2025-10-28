package com.sinse.universe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ARTIST")
@Entity
@Data
@ToString(exclude = "partner")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_ID")
    private int id;

    @Column(name = "AR_NAME")
    private String name;

    @Column(name = "AR_DESC")
    private String description;

    @Column(name = "AR_IMG")
    private String img;

    @Column(name = "AR_LOGO_IMG")
    private String logoImg;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "AR_DEBUT_DATE")
    private LocalDate debutDate;

    @Column(name = "AR_INSTA")
    private String insta;

    @Column(name = "AR_YOUTUBE")
    private String youtube;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PT_ID")
    @JsonIgnore  // JSON 응답에서 제외
    private Partner partner;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Color> colors = new ArrayList<>();

}
