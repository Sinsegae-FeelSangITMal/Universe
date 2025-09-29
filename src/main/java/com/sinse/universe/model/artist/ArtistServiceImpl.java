package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Partner;
import com.sinse.universe.dto.request.ArtistRequest;
import com.sinse.universe.dto.response.PartnerArtistResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.partner.PartnerRepository;
import com.sinse.universe.util.UploadManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ArtistServiceImpl implements ArtistService {

    public final ArtistRepository artistRepository;
    private final PartnerRepository partnerRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository, PartnerRepository partnerRepository) {
        this.artistRepository = artistRepository;
        this.partnerRepository = partnerRepository;
    }

    @Value("${upload.base-dir}")
    private String baseDir;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @Value("${upload.artist-main-dir}")
    private String artistMainDir;

    @Value("${upload.artist-logo-dir}")
    private String artistLogoDir;

    @Value("${upload.artist-main-url}")
    private String artistMainUrl;

    @Value("${upload.artist-logo-url}")
    private String artistLogoUrl;

    // 아티스트 전체 조회
    @Override
    public List<Artist> selectAll() {return artistRepository.findAll();}

    // 아티스트 1건 조회
    @Override
    public Artist select(int artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));
    }

    // 아티스트 등록
    @Override
    @Transactional
    public void regist(ArtistRequest request) {
        Partner partner = partnerRepository.findById(request.partnerId())
                .orElseThrow(() -> new CustomException(ErrorCode.PARTNER_NOT_FOUND));

        // ✅ 이름 중복 검사
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

    // 아티스트 수정
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

        // 메인 이미지 삭제
        if (deleteMainImage && existing.getImg() != null) {
            Path oldPath = Paths.get(baseDir).resolve(existing.getImg().replaceFirst("^" + urlPrefix + "/", ""));
            try {
                Files.deleteIfExists(oldPath);
            } catch (IOException e) {
                log.error("메인 이미지 삭제 실패 path={}", oldPath, e);
                // ❗ API 전체 실패 대신 로그만 남기고 DB만 갱신
            }
            existing.setImg(null);
        }

        // 로고 이미지 삭제
        if (deleteLogoImage && existing.getLogoImg() != null) {
            Path oldPath = Paths.get(baseDir).resolve(existing.getLogoImg().replaceFirst("^" + urlPrefix + "/", ""));
            try {
                Files.deleteIfExists(oldPath);
            } catch (IOException e) {
                log.error("로고 이미지 삭제 실패 path={}", oldPath, e);
                // ❗ 동일하게 로그만 남기고 진행
            }
            existing.setLogoImg(null);
        }

        // 새 메인 이미지 업로드
        if (mainImage != null && !mainImage.isEmpty()) {

            String mainDir = artistMainDir + "/a" + existing.getId();
            String mainFilename = UploadManager.storeAndReturnName(mainImage, mainDir);
            existing.setImg(artistMainUrl + "/a" + existing.getId() + "/" + mainFilename);
        }

        // 새 로고 이미지 업로드
        if (logoImage != null && !logoImage.isEmpty()) {
            String logoDir = artistLogoDir + "/a" + existing.getId();
            String logoFilename = UploadManager.storeAndReturnName(logoImage, logoDir);
            existing.setLogoImg(artistLogoUrl + "/a" + existing.getId() + "/" + logoFilename);
        }

        artistRepository.save(existing);
    }

    // 아티스트 삭제
    @Override
    @Transactional
    public void delete(int artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));

        // 🔹 연관 데이터 검증
        if (artist.getMembers() != null && !artist.getMembers().isEmpty()) {
            throw new CustomException(ErrorCode.ARTIST_DELETE_NOT_ALLOWED);
        }

        artistRepository.delete(artist);
    }

    // 소속사(Partner) ID로 아티스트 정보 조회
    @Override
    public List<Artist> findByPartnerId(int partnerId) {
        return artistRepository.findByPartnerId(partnerId);
    }

    //소속사(Partner) ID로 아티스트 이름만 조회
    @Override
    public List<PartnerArtistResponse> selectByPartnerId(int partnerId) {
        return artistRepository.findByPartner_Id(partnerId).stream()
                .map(PartnerArtistResponse::from)
                .toList();
    }
}
