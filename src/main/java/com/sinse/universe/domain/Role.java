package com.sinse.universe.domain;

import com.sinse.universe.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RO_ID")
    private int id;

    @Column(name = "RO_NAME", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private UserRole role;

}
