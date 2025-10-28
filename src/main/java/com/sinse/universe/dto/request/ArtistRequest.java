package com.sinse.universe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ArtistRequest(

        @NotBlank(message = "아티스트 이름은 필수입니다.")
        String name,

        String description,

        LocalDate debutDate,

        String insta,

        String youtube,

        int partnerId,

        // 이미지 삭제 여부 플래그 (null 허용, 기본값 false로 컨트롤러에서 처리 가능)
        Boolean deleteMainImage,
        Boolean deleteLogoImage
) {}
