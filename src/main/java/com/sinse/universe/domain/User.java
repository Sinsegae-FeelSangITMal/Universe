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

    @Column(name="UR_LOGIN_ID", nullable = false, unique = true)
    private String loginId;

    @Column(name = "UR_PWD", nullable = false)
    private String password;

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
