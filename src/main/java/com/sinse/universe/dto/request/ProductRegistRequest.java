package com.sinse.universe.dto.request;

import com.sinse.universe.domain.Product;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record ProductRegistRequest(
        Integer productId,

        @NotBlank @Size(max = 200)
        String productName,

        @Size(max = 2000)
        String detail,

        @NotNull @PositiveOrZero
        Integer price,

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime salesOpenAt,

        Boolean fanLimited,

        @NotNull @PositiveOrZero
        Integer initialStock,

        @NotNull @PositiveOrZero
        Integer purchaseLimit,

        Boolean promotion,

        Product.ProductStatus  productStatus,

        @NotNull
        Integer categoryId,

        @NotNull
        Integer artistId
) {
    public ProductRegistRequest {
        if (fanLimited == null) fanLimited = false;
        if (initialStock == null) initialStock = 0;
        if (purchaseLimit == null) purchaseLimit = 0;
        if (promotion == null) promotion = false;
    }
}
