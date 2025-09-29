package com.sinse.universe.domain;

import com.sinse.universe.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UR_ID")
    private int id;

    @Column(name = "UR_EMAIL")
    private String email;

    @Column(name = "UR_PWD")
    private String password;

    @Column(name = "UR_PROVIDER")
    private String provider;

    @Column(name = "UR_OAUTH_ID")
    private String oauthId;

    @Column(name = "UR_NAME", nullable = false)
    private String name;

    @Column(name = "UR_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "UR_JOIN_DATE", nullable = false, insertable = false, updatable = false)
    private LocalDateTime joinDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RO_ID", nullable = false)
    private Role role;
}
