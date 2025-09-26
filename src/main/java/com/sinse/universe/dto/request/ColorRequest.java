package com.sinse.universe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ColorRequest(

        @NotBlank(message = "배경 색상은 필수입니다.")
        String bgColor,

        @NotNull(message = "아티스트 ID는 필수입니다.")
        Integer artistId
) {}
