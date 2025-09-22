package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Artist;

import java.time.LocalDate;
import java.util.List;

public record ArtistResponse(
        int id,
        String name,
        String description,
        String img,
        String logoImg,
        LocalDate debutDate,
        String insta,
        String youtube,
        String partnerName,            // partner 엔티티 대신 이름만
        List<MemberResponse> members   // 멤버 리스트
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
                artist.getPartner() != null ? artist.getPartner().getName() : null,
                artist.getMembers() != null
                        ? artist.getMembers().stream()
                        .map(MemberResponse::from)
                        .toList()
                        : List.of()
        );
    }
}
