package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Artist;

import java.time.LocalDate;

public record ArtistResponse(
        int id,
        String name,
        String description,
        String img,
        String logoImg,
        LocalDate debutDate,
        String insta,
        String youtube,
        String partnerName
        // , List<MemberResponse> members  // 필요할 때만 포함
) {
    public static ArtistResponse from(Artist artist) {
        return new ArtistResponse(
                artist.getId(),
                artist.getName(),
                artist.getDescription(),
                artist.getImg(),
                artist.getLogoImg(),
                artist.getDebutDate(),
                artist.getInsta(),
                artist.getYoutube(),
                artist.getPartner() != null ? artist.getPartner().getName() : null
        );
    }
}