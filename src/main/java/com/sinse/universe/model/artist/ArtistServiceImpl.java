package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;
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
import java.util.List;

@Slf4j
@Service
public class ArtistServiceImpl implements ArtistService{

    public final ArtistRepository artistRepository;
    private final PartnerRepository partnerRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository, PartnerRepository partnerRepository) {this.artistRepository = artistRepository;
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
    public Artist select(int artistId) {return artistRepository.findById(artistId).orElse(null);}

    // 아티스트 등록
    @Override
    @Transactional
    public void regist(Artist artist) {
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
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        existing.setName(artist.getName());
        existing.setDescription(artist.getDescription());
        existing.setDebutDate(artist.getDebutDate());
        existing.setInsta(artist.getInsta());
        existing.setYoutube(artist.getYoutube());
        existing.setPartner(artist.getPartner());

        // 메인 이미지 삭제
        if (deleteMainImage && existing.getImg() != null) {
            Path oldPath = Paths.get(baseDir).resolve(existing.getImg().replaceFirst("^" + urlPrefix + "/", ""));

            Files.deleteIfExists(oldPath);
            existing.setImg(null);
        }

        // 로고 이미지 삭제
        if (deleteLogoImage && existing.getLogoImg() != null) {
            Path oldPath = Paths.get(baseDir).resolve(existing.getImg().replaceFirst("^" + urlPrefix + "/", ""));

            Files.deleteIfExists(oldPath);
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
    public void delete(int artistId) {
        artistRepository.deleteById(artistId);
    }

    // 소속사(Partner) ID로 아티스트 조회
    @Override
    public List<Artist> findByPartnerId(int partnerId) {
        return artistRepository.findByPartnerId(partnerId);
    }

}
