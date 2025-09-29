package com.sinse.universe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ARTIST_MEMBER")
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_MB_ID")
    private int id;  // 멤버 고유 ID

    @Column(name = "AR_MB_NAME", nullable = false, length = 100)
    private String name;  // 멤버 이름

    @Column(name = "AR_MB_IMG", length = 255)
    private String img;  // 멤버 이미지 (nullable)

    // Artist 와 다대일 관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AR_ID", nullable = false)
    @JsonIgnore
    private Artist artist;
}
