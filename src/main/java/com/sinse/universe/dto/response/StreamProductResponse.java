package com.sinse.universe.dto.response;

import com.sinse.universe.domain.StreamProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public record StreamProductResponse(
        Integer id,
        ProductResponse product
) {
    public static StreamProductResponse from(StreamProduct sp) {
        if (sp == null) return null;

        return new StreamProductResponse(
                sp.getId(),
                ProductResponse.from(sp.getProduct())
        );
    }
}
