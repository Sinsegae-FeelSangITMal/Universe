package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Product;

public record CartProductResponse(
        String name,
        Integer price,
        String artistName,
        String partnerName
) {
    public static CartProductResponse from(Product p) {
        return new CartProductResponse(
                p.getName(),
                p.getPrice(),
                p.getArtist() != null ? p.getArtist().getName() : null,
                p.getArtist() != null ? p.getArtist().getPartner().getName() : null
        );
    }
}