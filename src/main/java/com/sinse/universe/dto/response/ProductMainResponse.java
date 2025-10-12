package com.sinse.universe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.ProductImage;

import java.time.LocalDateTime;

public record ProductMainResponse(
        int id,
        String productName,
        int price,
        Boolean fanOnly,
        int stockQty,
        String mainImageUrl,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDateTime registDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime openDate
) {
    public static ProductMainResponse from(Product p) {
        String mainUrl = null;
        if (p.getProductImageList() != null) {
            var mainOpt = p.getProductImageList().stream()
                    .filter(pi -> pi.getRole() == ProductImage.Role.MAIN)
                    .findFirst();
            if (mainOpt.isPresent()) mainUrl = mainOpt.get().getUrl();
        }

        return new ProductMainResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getFanOnly(),
                p.getStockQuantity(),
                mainUrl,
                p.getRegistDate(),
                p.getOpenDate()
        );
    }
}
