package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Map<String, Object>> findAllArtistNameAndIdByPartner(Integer partnerId) {
        return artistRepository.findAllArtistNameAndIdByPartnerId(partnerId)
                .stream()
                .map(a -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", a.getId());
                    map.put("name", a.getName());
                    return map;
                })
                .toList();
    }

}
