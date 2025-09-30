package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Membership;

import java.time.LocalDateTime;

public record MembershipResponse (
    String artistName,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
    public static MembershipResponse from(Membership m) {
        return new MembershipResponse(
                m.getArtist().getName(),
                m.getStartDate(),
                m.getEndDate()
        );
    }
}
