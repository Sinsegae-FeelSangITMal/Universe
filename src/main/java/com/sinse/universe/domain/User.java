package com.sinse.universe.domain;

import com.sinse.universe.enums.UserRole;
import com.sinse.universe.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="USERS")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UR_ID")
    private int id;

    @Column(name="UR_LOGIN_ID")
    private String loginId;

    @Column(name = "UR_PWD")
    private String password;

    @Column(name = "UR_EMAIL")
    private String email;

    @Column(name = "UR_PHONE")
    private String phone;

    @Column(name = "UR_NAME")
    private String name;

    @Column(name = "UR_STATUS")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "UR_JOIN_DATE")
    private LocalDateTime joinDate;

    @Column(name = "UR_ROLE")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
