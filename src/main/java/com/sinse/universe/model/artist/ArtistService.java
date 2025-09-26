package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;

import java.util.List;
import java.util.Map;

public interface ArtistService {

    public List<Map<String, Object>> findAllArtistNameAndIdByPartner(Integer partnerId);

}
