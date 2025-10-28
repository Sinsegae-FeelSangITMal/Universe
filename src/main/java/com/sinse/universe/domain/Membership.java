package com.sinse.universe.domain;

import com.sinse.universe.enums.MembershipStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBERSHIP")
@Data
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MS_ID")
    private int id;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UR_ID", nullable = false)
    private User user;

    // 아티스트
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AR_ID", nullable = false)
    private Artist artist;

    @Column(name = "MS_START_DATE", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "MS_END_DATE", nullable = false)
    private LocalDateTime endDate;
}
