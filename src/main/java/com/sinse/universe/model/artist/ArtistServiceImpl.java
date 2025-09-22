package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Partner;
import com.sinse.universe.model.partner.PartnerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService{

    public final ArtistRepository artistRepository;
    private final PartnerRepository partnerRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository, PartnerRepository partnerRepository) {this.artistRepository = artistRepository;
        this.partnerRepository = partnerRepository;
    }

    @Override
    public List<Artist> selectAll() {return artistRepository.findAll();}

    @Override
//    public Artist select(int artistId) {return artistRepository.findById(artistId).orElse(null);}
    public Artist select(int id) {
        return artistRepository.findByIdWithMembers(id)  // JOIN FETCH 버전 사용
                .orElseThrow(() -> new EntityNotFoundException("Artist not found"));
    }

    @Override
    public void regist(Artist artist) {
        artistRepository.save(artist);
    }

    @Override
    public void update(Artist artist) {
        // DB에서 기존 Artist 가져오기
        Artist existing = artistRepository.findById(artist.getId())
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        // 변경 가능한 필드만 update
        existing.setName(artist.getName());
        existing.setDescription(artist.getDescription());
        existing.setImg(artist.getImg());
        existing.setLogoImg(artist.getLogoImg());
        existing.setDebutDate(artist.getDebutDate());
        existing.setInsta(artist.getInsta());
        existing.setYoutube(artist.getYoutube());

        // ✅ partner는 기존 partner 유지 (null로 덮어쓰지 않음)
        if (artist.getPartner() != null && artist.getPartner().getId() > 0) {
            Partner partner = partnerRepository.findById(artist.getPartner().getId())
                    .orElseThrow(() -> new RuntimeException("Partner not found"));
            existing.setPartner(partner);
        }

        artistRepository.save(existing);
    }

    @Override
    public void delete(int artistId) {
        artistRepository.deleteById(artistId);
    }

    @Override
    public List<Artist> findByPartnerId(int partnerId) {
        return artistRepository.findByPartnerId(partnerId);
    }


}
