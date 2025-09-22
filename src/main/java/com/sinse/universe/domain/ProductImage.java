package com.sinse.universe.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT_IMAGE")
@Data
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PI_ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PD_ID", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "PI_ROLE", nullable = false, length = 10)
    private Role role = Role.DETAIL; // 기본값: DETAIL

    @Column(name = "PI_URL", nullable = false, length = 500)
    private String url;

    @Column(name = "PI_ORG_NAME", length = 255)
    private String originalName;

    @Column(name = "PI_MIME", length = 100)
    private String mimeType;

    @Column(name = "PI_SIZE")
    private Long size;

    @Column(name = "created_at", insertable = false, updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public enum Role {
        MAIN, DETAIL
    }
}
