package com.sinse.universe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sinse.universe.enums.StreamStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STREAM")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Stream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SR_ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AR_ID", nullable = false)
    @JsonIgnore
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PM_ID")   // UNIQUE 제거
    private Promotion promotion;

    @Column(name = "SR_TITLE", nullable = false, length = 200)
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "SR_TIME", nullable = false)
    private LocalDateTime time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "SR_END_TIME")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "SR_STATUS", nullable = false, length = 10)
    private StreamStatus status = StreamStatus.WAITING;

    @Column(name = "SR_IS_AIR")
    private boolean isAir = false;

    @Column(name = "SR_FAN_ONLY")
    private boolean fanOnly = false;

    // Stream.java
    @Column(name = "SR_PROD_LINK")
    private boolean prodLink;

    @Column(name = "SR_PR_YN")
    private boolean prYn;

    @Column(name = "SR_VIEW_CNT")
    private Integer viewCnt;

    @Column(name = "SR_LIKE_CNT")
    private Integer likeCnt;

    @Column(name = "SR_THUMB", length = 200)
    private String thumb;

    @Column(name = "SR_RECORD", length = 500)
    private String record;

    @OneToMany(mappedBy = "stream", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StreamProduct> streamProducts = new ArrayList<>();
}
