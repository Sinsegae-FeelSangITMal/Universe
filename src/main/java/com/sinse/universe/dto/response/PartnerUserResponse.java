package com.sinse.universe.dto.response;

import com.sinse.universe.domain.PartnerUser;
import com.sinse.universe.domain.User;

public record PartnerUserResponse (
    int partnerId,
    String partnerName,
    int userId,
    String userName,
    String roleName,
    String email
){
    public static PartnerUserResponse from(PartnerUser p) {
        return new PartnerUserResponse(
                p.getPartner().getId(),
                p.getPartner().getName(),
                p.getUser().getId(),
                p.getUser().getName(),
                p.getUser().getRole().getName().name(),
                p.getUser().getEmail()
        );
    }
}
