package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Partner;
import com.sinse.universe.dto.request.ArtistRequest;
import com.sinse.universe.dto.response.PartnerArtistResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.partner.PartnerRepository;
import com.sinse.universe.util.ObjectStorageService; // ✅
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final PartnerRepository partnerRepository;
    private final ObjectStorageService objectStorageService; // ✅

    public ArtistServiceImpl(ArtistRepository artistRepository,
                             PartnerRepository partnerRepository,
                             ObjectStorageService objectStorageService) {
        this.artistRepository = artistRepository;
        this.partnerRepository = partnerRepository;
        this.objectStorageService = objectStorageService;
    }

    @Value("${upload.artist-main-url}") // 예: /images/artist/main
    private String artistMainUrl;

    @Value("${upload.artist-logo-url}") // 예: /images/artist/logo
    private String artistLogoUrl;

    @Override
    public List<Artist> selectAll() {
        return artistRepository.findAll();
    }

    @Override
    public Artist select(int artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));
    }

    @Override
    @Transactional
    public void regist(ArtistRequest request) {
        Partner partner = partnerRepository.findById(request.partnerId())
                .orElseThrow(() -> new CustomException(ErrorCode.PARTNER_NOT_FOUND));

        if (artistRepository.existsByName(request.name())) {
            throw new CustomException(ErrorCode.ARTIST_NAME_DUPLICATED);
        }

        Artist artist = new Artist();
        artist.setName(request.name());
        artist.setDescription(request.description());
        artist.setPartner(partner);
        artist.setDebutDate(request.debutDate());
        artist.setInsta(request.insta());
        artist.setYoutube(request.youtube());

        artistRepository.save(artist);
    }

    @Override
    @Transactional
    public void update(Artist artist,
                       MultipartFile mainImage,
                       MultipartFile logoImage,
                       boolean deleteMainImage,
                       boolean deleteLogoImage) throws IOException {

        Artist existing = artistRepository.findById(artist.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));

        existing.setName(artist.getName());
        existing.setDescription(artist.getDescription());
        existing.setDebutDate(artist.getDebutDate());
        existing.setInsta(artist.getInsta());
        existing.setYoutube(artist.getYoutube());
        existing.setPartner(artist.getPartner());

        // 삭제 플래그 처리
        if (deleteMainImage && existing.getImg() != null) {
            deleteObjectByUrl(existing.getImg());
            existing.setImg(null);
        }
        if (deleteLogoImage && existing.getLogoImg() != null) {
            deleteObjectByUrl(existing.getLogoImg());
            existing.setLogoImg(null);
        }

        // 메인 이미지 업로드(교체)
        if (mainImage != null && !mainImage.isEmpty()) {
            if (existing.getImg() != null) deleteObjectByUrl(existing.getImg());
            String key = objectStorageService.store(mainImage, "artist/main/a" + existing.getId());
            existing.setImg("/images/" + key); // ✅ DB에는 /images/{key}
        }

        // 로고 이미지 업로드(교체)
        if (logoImage != null && !logoImage.isEmpty()) {
            if (existing.getLogoImg() != null) deleteObjectByUrl(existing.getLogoImg());
            String key = objectStorageService.store(logoImage, "artist/logo/a" + existing.getId());
            existing.setLogoImg("/images/" + key); // ✅
        }

        artistRepository.saveAndFlush(existing);
    }

    @Override
    @Transactional
    public void delete(int artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));

        if (artist.getMembers() != null && !artist.getMembers().isEmpty()) {
            throw new CustomException(ErrorCode.ARTIST_DELETE_NOT_ALLOWED);
        }

        if (artist.getImg() != null) deleteObjectByUrl(artist.getImg());
        if (artist.getLogoImg() != null) deleteObjectByUrl(artist.getLogoImg());

        artistRepository.delete(artist);
    }

    @Override
    public List<Artist> findByPartnerId(int partnerId) {
        return artistRepository.findByPartnerId(partnerId);
    }

    @Override
    public List<PartnerArtistResponse> selectByPartnerId(int partnerId) {
        return artistRepository.findByPartner_Id(partnerId).stream()
                .map(PartnerArtistResponse::from)
                .toList();
    }

    /** /images/{objectKey} → objectKey 추출 */
    private String extractKey(String url) {
        if (url == null) return null;
        final String p = "/images/";
        return url.startsWith(p) ? url.substring(p.length()) : null;
    }

    private void deleteObjectByUrl(String url) {
        String key = extractKey(url);
        if (key != null && !key.isBlank()) {
            objectStorageService.deleteObject(key);
        }
    }
}
