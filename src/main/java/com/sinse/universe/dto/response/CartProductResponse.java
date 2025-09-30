package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Product;

public record CartProductResponse(
        Integer id,
        String name,
        Integer price,
        String artistName,
        String partnerName,
        Integer limit,
        Integer stock,
        String mainImageUrl,            // 대표 이미지
        String description
) {
    public static CartProductResponse from(Product p, String mainImageUrl) {
        return new CartProductResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getArtist() != null ? p.getArtist().getName() : null,
                p.getArtist() != null ? p.getArtist().getPartner().getName() : null,
                p.getLimitPerUser(),
                p.getStockQuantity(),
                mainImageUrl,
                p.getDescription()
        );
    }
}