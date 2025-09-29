package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Color;

public record ColorResponse(
        int id,
        String bgColor,
        String artistName
) {
    public static ColorResponse from(Color color) {
        return new ColorResponse(
                color.getId(),
                color.getBgColor(),
                color.getArtist() != null ? color.getArtist().getName() : null
        );
    }
}