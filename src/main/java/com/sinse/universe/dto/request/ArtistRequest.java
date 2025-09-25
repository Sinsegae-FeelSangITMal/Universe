package com.sinse.universe.dto.request;

import java.time.LocalDate;

public record ArtistRequest(
        String name,
        String description,
        LocalDate debutDate,
        String insta,
        String youtube,
        int partnerId,

        // 이미지 삭제 여부 플래그
        Boolean deleteMainImage,
        Boolean deleteLogoImage
) {}
