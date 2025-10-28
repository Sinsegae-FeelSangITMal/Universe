package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Member;

public record MemberResponse(
        int id,
        String name,
        String img,
        int artistId
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getImg(),
                member.getArtist() != null ? member.getArtist().getId() : 0
        );
    }
}

