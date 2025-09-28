package com.sinse.universe.dto.response;

import com.sinse.universe.domain.StreamProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreamProductResponse {

    private int id;              // SR_PD_ID (PK)
    private Integer streamId;    // 연결된 Stream ID
    private Integer productId;   // 연결된 Product ID
    private String productName;  // 상품명
    private String productDesc;  // 상품 설명 (옵션)
    private Integer productPrice; // 상품 가격 (옵션)

    // 단일 변환
    public static StreamProductResponse from(StreamProduct sp) {
        if (sp == null) return null;

        return StreamProductResponse.builder()
                .id(sp.getId())
                .streamId(sp.getStream() != null ? sp.getStream().getId() : null)
                .productId(sp.getProduct() != null ? sp.getProduct().getId() : null)
                .productName(sp.getProduct() != null ? sp.getProduct().getName() : null)
                .productDesc(sp.getProduct() != null ? sp.getProduct().getDescription() : null) // Product 엔티티에 description 있다면
                .productPrice(sp.getProduct() != null ? sp.getProduct().getPrice() : null) // Product 엔티티에 price 있다면
                .build();
    }

    // 리스트 변환
    public static List<StreamProductResponse> fromList(List<StreamProduct> streamProducts) {
        return streamProducts.stream()
                .map(StreamProductResponse::from)
                .collect(Collectors.toList());
    }
}
