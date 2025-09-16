package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Table(name = "ARTIST")
@Entity
@Data
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_ID")
    private int id;

    @Column(name = "AR_NAME")
    private String name;

    @Column(name = "AR_DESC")
    private String desc;

    @Column(name = "AR_IMG")
    private String imgUrl;

    @Column(name = "AR_LOGO_IMG")
    private String logoImgUrl;

    @Column(name = "AR_DEBUT_DATE")
    private Date debutDate;

    @Column(name = "AR_INSTA")
    private String instaUrl;

    @Column(name = "AR_YOUTUBE")
    private String youtubeUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PT_ID")
    private Partner partner;
}
