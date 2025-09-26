package com.sinse.universe.domain;

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

    // 현재 이름 컬럼만 사용 중이라 다 바꿔도 돼여 - 승연
    @Column(name = "UR_NAME")
    private String name;

}
