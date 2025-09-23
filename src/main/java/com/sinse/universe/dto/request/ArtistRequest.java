package com.sinse.universe.dto.request;

import java.time.LocalDate;

public record ArtistRequest(
        String name,
        String description,
        Integer partnerId,
        LocalDate debutDate,   // 데뷔일 (LocalDate 추천)
        String insta,          // 인스타그램 URL
        String youtube         // 유튜브 URL
) {}