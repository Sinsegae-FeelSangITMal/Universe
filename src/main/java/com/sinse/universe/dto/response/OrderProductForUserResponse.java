package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Membership;
import com.sinse.universe.domain.OrderProduct;
import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.ProductImage;

import java.time.LocalDateTime;

public record OrderProductForUserResponse(
        Integer qty,
        String name,
        String artistName,
        Integer price,
        String mainImageUrl,
        String description,
        String category,
        LocalDateTime membershipStartDate,
        LocalDateTime membershipEndDate
) {
    // 멤버십이 있을 때 사용하는 팩토리 메서드
    public static OrderProductForUserResponse from(OrderProduct op, Membership membership) {
        Product p = op.getProduct();

        // Product 에서 메인 이미지 가져오기
        String mainImageUrl = p.getProductImageList().stream()
                .filter(img -> img.getRole() == ProductImage.Role.MAIN)
                .findFirst()
                .map(ProductImage::getUrl)
                .orElse(null);

        return new OrderProductForUserResponse(
                op.getQty(),
                p.getName(),
                p.getArtist().getName(),
                op.getPrice(),
                mainImageUrl,
                p.getDescription(),
                p.getCategory().getName(),
                membership != null ? membership.getStartDate() : null,
                membership != null ? membership.getEndDate() : null
        );
    }

    // 멤버십 없는 일반 상품일 때 사용하는 오버로드 메서드
    public static OrderProductForUserResponse from(OrderProduct op) {
        return from(op, null);
    }
}
