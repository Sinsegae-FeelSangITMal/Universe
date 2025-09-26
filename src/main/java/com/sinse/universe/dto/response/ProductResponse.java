package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String name,
        Integer price,
        Integer stockQty,
        String categoryName,
        String artistName
) {
    public static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getStockQuantity(),
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getArtist()   != null ? p.getArtist().getName()   : null
        );
    }
}