package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Partner;

public record PartnerResponse(
        int id,
        String name,
        String address,
        String file
//        , List<ArtistResponse> artists
) {
    public static PartnerResponse from(Partner partner) {
        return new PartnerResponse(
                partner.getId(),
                partner.getName(),
                partner.getAddress(),
                partner.getFile()
//                , partner.getArtists() != null
//                        ? partner.getArtists().stream()
//                        .map(ArtistResponse::from)
//                        .toList()
//                        : List.of()
        );
    }
}
