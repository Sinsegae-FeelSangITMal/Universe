package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Table(name = "ARTIST_MEMBER")
@Entity
@Data
public class ArtistMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_MB_ID")
    private int id;

    @Column(name = "AR_MB_NAME", nullable = false)
    private String name;

    @Column(name = "AR_MB_IMG")
    private String img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AR_ID", nullable = false)
    private Artist artist;
}
