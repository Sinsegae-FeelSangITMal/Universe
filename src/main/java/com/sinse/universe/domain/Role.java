package com.sinse.universe.domain;

import com.sinse.universe.enums.RoleName;
import com.sinse.universe.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="ROLE")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RO_ID")
    private int id;

    @Column(name="RO_NAME", nullable=false, unique=true)
    @Enumerated(EnumType.STRING)
    private RoleName name;
}
