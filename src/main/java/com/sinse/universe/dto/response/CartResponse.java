package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Cart;
import com.sinse.universe.domain.Order;
import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.ProductImage;
import com.sinse.universe.enums.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record CartResponse(
        Integer id,
        Integer qty,
        CartProductResponse product,
        Integer userId
        ) {
    public static CartResponse from(Cart c) {
        Product p = c.getProduct();

        // Product 에서 이미지 리스트 가져오기
        String mainImageUrl = p.getProductImageList().stream()
                .filter(img -> img.getRole() == ProductImage.Role.MAIN)
                .findFirst()
                .map(ProductImage::getUrl)
                .orElse(null);

        return new CartResponse(
                c.getId(),
                c.getQty(),
                CartProductResponse.from(p, mainImageUrl),
                c.getUser().getId()
        );
    }
}