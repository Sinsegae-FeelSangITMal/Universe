package com.sinse.universe.domain;

import com.sinse.universe.enums.PartnerRole;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PARTNER_USER")   // 소속사 관계자
public class PartnerUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PT_US_ID")
    private int id;

    @Column(name = "PT_ROLE")
    @Enumerated(EnumType.STRING) // String은 "ADMIN, MANAGER"로 매핑, EnumType.ORDINAL 0, 1, 2 숫자로 저장됨
    private PartnerRole role;

    @OneToOne
    @JoinColumn(name = "UR_ID")  //DB 컬럼명
    private User user;

    @ManyToOne
    @JoinColumn(name = "PT_ID")
    private Partner partner;

}
