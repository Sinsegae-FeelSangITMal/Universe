package com.sinse.universe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

import java.util.List;
public record OrderSubmitRequest(
        @NotNull Integer userId,
        @NotNull Orderer orderer,
        @NotNull Receiver receiver,
        @NotBlank String paymentMethod,
        @NotNull List<OrderProductRequest> items,
        @NotNull Integer totalPrice,
        boolean agree
) {
    public record Orderer(
            @NotBlank String name,
            @Email String email
    ) {}

    public record Receiver(
            @NotBlank String name,
            @NotBlank String phone,
            @NotBlank String address,
            String addressDetail,
            @NotBlank String country,
            @NotBlank String city,
            String state,
            @NotBlank String postal
    ) {}

    public record OrderProductRequest(
            @NotNull Integer productId,
            @NotNull Integer qty,
            @NotNull Integer price
    ) {}
}
