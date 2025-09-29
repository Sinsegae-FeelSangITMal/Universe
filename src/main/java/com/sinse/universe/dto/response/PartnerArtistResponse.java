package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Artist;

public record PartnerArtistResponse(
        Integer id,
        String name
        ) {
    public static PartnerArtistResponse from(Artist a) {
        return new PartnerArtistResponse(
                a.getId(),
                a.getName()
        );
    }
}