package com.sinse.universe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PromotionRegistRequest {

    @NotBlank(message = "프로모션 이름은 필수입니다.")
    private String name;

    private String description;

    private BigDecimal price;

    private boolean fanOnly;

    private int stockQty;

    private int limitPerUser;

    private String coupon;

    @NotNull(message = "아티스트 ID는 필수입니다.")
    private Integer artistId;
}
