package com.sinse.universe.model.artist;

import com.sinse.universe.dto.response.PartnerArtistResponse;

import java.util.List;

public interface ArtistService {
    public List<PartnerArtistResponse> selectByPartnerId (Integer partnerId);
}
