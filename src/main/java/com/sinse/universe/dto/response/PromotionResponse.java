package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Promotion;

import java.math.BigDecimal;

public record PromotionResponse(
        Integer id,
        String name,
        String description,
        String img,
        Integer price,
        boolean fanOnly,
        int stockQty,
        int limitPerUser,
        String coupon,
        String artistName
) {
    public static PromotionResponse from(Promotion p) {
        return new PromotionResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getImg(),
                p.getPrice(),
                p.isFanOnly(),
                p.getStockQty(),
                p.getLimitPerUser(),
                p.getCoupon(),
                (p.getArtist() != null ? p.getArtist().getName() : null)
        );
    }
}
