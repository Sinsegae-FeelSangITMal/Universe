package com.sinse.universe.model.artist;

import com.sinse.universe.dto.response.PartnerArtistResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<PartnerArtistResponse> selectByPartnerId(Integer partnerId) {
        return artistRepository.findByPartner_Id(partnerId).stream()
                .map(PartnerArtistResponse::from)
                .toList();
    }
}
