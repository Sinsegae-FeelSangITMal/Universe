package com.sinse.universe.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sinse.universe.domain.Artist;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "ARTIST_BG_COLOR")
@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_BG_ID")
    private int id;

    @Column(name = "AR_BG_COLOR", nullable = false, length = 50)
    private String bgColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AR_ID", nullable = false)
    @JsonIgnore  // ✅ 순환참조 방지
    private Artist artist;
}
