package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String name,
        String description,   // PD_DESC
        Integer price,        // PD_PRICE
        Integer stockQty,     // PD_STOCK_QTY
        Integer limitPerUser, // PD_LIMIT_PER_USER
        Boolean fanOnly,      // PD_FAN_ONLY
        String status,        // PD_STATUS
        String categoryName,  // 카테고리명
        String artistName     // 아티스트명
) {
    public static ProductResponse from(Product p) {
        if (p == null) return null;

        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStockQuantity(),
                p.getLimitPerUser(),
                p.getFanOnly(),
                p.getStatus() != null ? p.getStatus().name() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getArtist()   != null ? p.getArtist().getName()   : null
        );
    }
}