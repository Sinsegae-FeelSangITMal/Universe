package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Product;

import java.util.List;

public record ProductDetailResponse(
        Integer id,
        String productName,
        Integer price,
        String detail,
        String salesOpenAt,
        Boolean fanLimited,
        Integer initialStock,
        Integer purchaseLimit,
        Boolean promotion,
        Integer artistId,
        Integer categoryId,
        Integer subcategoryId,
        String mainImageUrl,            // 대표 이미지
        List<ImageDto> detailImages     // 상세 이미지들
) {
    public static ProductDetailResponse from(Product p) {
        return new ProductDetailResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getDescription(),
                String.valueOf(p.getOpenDate()), // 타입이 String이 아니어도 안전
                p.getFanOnly(),
                p.getStockQuantity(),
                p.getLimitPerUser(),
                p.getPromotion(),
                p.getArtist() != null ? p.getArtist().getId() : null,
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getSubcategory() != null ? p.getSubcategory().getId() : null,
                null,
                List.of()
        );
    }

    public static ProductDetailResponse from(Product p, String mainImageUrl, List<ImageDto> detailImages) {
        return new ProductDetailResponse(
                p.getId(),
                p.getProductName(),
                p.getPrice(),
                p.getDetail(),
                String.valueOf(p.getSalesOpenAt()),
                p.getFanLimited(),
                p.getInitialStock(),
                p.getPurchaseLimit(),
                p.getPromotion(),
                p.getArtist() != null ? p.getArtist().getId() : null,
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getSubcategory() != null ? p.getSubcategory().getId() : null,
                mainImageUrl,
                detailImages
        );
    }

    public static record ImageDto(Integer id, String url) {}
}
