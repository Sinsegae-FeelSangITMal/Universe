package com.sinse.universe.controller;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Partner;
import com.sinse.universe.dto.request.ArtistRequest;
import com.sinse.universe.dto.response.ArtistResponse;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.artist.ArtistService;
import com.sinse.universe.model.partner.PartnerRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api")
public class ArtistController {
    private final ArtistService artistService;
    private final ArtistRepository artistRepository;
    private final PartnerRepository partnerRepository;

    public ArtistController(ArtistService artistService, ArtistRepository artistRepository, PartnerRepository partnerRepository) {
        this.artistService = artistService;
        this.artistRepository = artistRepository;
        this.partnerRepository = partnerRepository;
    }

    // 전체 아티스트 조회
    @GetMapping("/ent/artists")
    public List<ArtistResponse> getArtists() {
        return artistRepository.findAll()
                .stream()
                .map(ArtistResponse::from)
                .toList();
    }

    // 상세 아티스트 조회
    @GetMapping("/ent/artists/{artistId}")
    public ArtistResponse getArtist(@PathVariable int artistId){
        Artist artist = artistService.select(artistId);
        return ArtistResponse.from(artist);
    }

    // 아티스트 등록
    @PostMapping("/ent/artists")
    public ResponseEntity<?> addArtist(@RequestBody ArtistRequest request) {
        artistService.regist(request);
        return ResponseEntity.ok(Map.of("result","아티스트 등록 성공"));
    }

    // 아티스트 수정
    @PutMapping(value = "/ent/artists/{artistId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateArtist(
            @PathVariable int artistId,
            @ModelAttribute ArtistRequest request,
            @RequestPart(required = false) MultipartFile mainImage,
            @RequestPart(required = false) MultipartFile logoImage) throws IOException {

        Artist artist = artistService.select(artistId);

        artist.setName(request.name());
        artist.setDescription(request.description());
        artist.setDebutDate(request.debutDate());
        artist.setInsta(request.insta());
        artist.setYoutube(request.youtube());

        // ✅ 삭제 플래그도 함께 전달
        artistService.update(artist, mainImage, logoImage,
                Boolean.TRUE.equals(request.deleteMainImage()),
                Boolean.TRUE.equals(request.deleteLogoImage()));

        return ResponseEntity.ok(Map.of("result", "아티스트 수정 성공"));
    }


    // 아티스트 삭제
    @DeleteMapping("/ent/artists/{artistId}")
    public ResponseEntity<?> deleteArtist(@PathVariable int artistId) {
        artistService.delete(artistId);

        return ResponseEntity.ok(Map.of("result","아티스트 삭제 성공"));
    }

    // 특정 파트너의 아티스트 조회 (쿼리 파라미터 방식)
    @GetMapping(value = "/ent/artists", params = "partnerId")
    public List<ArtistResponse> getArtistsByPartner(@RequestParam int partnerId) {
        return artistService.findByPartnerId(partnerId)
                .stream()
                .map(ArtistResponse::from)
                .toList();
    }
}
