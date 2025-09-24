package com.sinse.universe.controller;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Partner;
import com.sinse.universe.dto.request.ArtistRequest;
import com.sinse.universe.dto.response.ArtistResponse;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.artist.ArtistService;
import com.sinse.universe.model.partner.PartnerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
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
    @GetMapping("/artists")
    public List<ArtistResponse> getArtists() {
        return artistRepository.findAll()
                .stream()
                .map(ArtistResponse::from)
                .toList();
    }

    // 상세 아티스트 조회
    @GetMapping("/artists/{artistId}")
    public ArtistResponse getArtist(@PathVariable int artistId){
        Artist artist = artistService.select(artistId);
        return ArtistResponse.from(artist);
    }

    // 아티스트 등록
    @PostMapping("/artists")
    public ResponseEntity<?> addArtist(@RequestBody ArtistRequest request) {
        Partner partner = partnerRepository.findById(request.partnerId())
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        Artist artist = new Artist();
        artist.setName(request.name());
        artist.setDescription(request.description());
        artist.setPartner(partner);

        artistRepository.save(artist);

        return ResponseEntity.ok(Map.of("result","아티스트 등록 성공"));
    }

    // 아티스트 수정
    @PutMapping("/artists/{artistId}")
    public ResponseEntity<?> updateArtist(@RequestBody ArtistRequest request, @PathVariable int artistId) {
        Artist artist = artistService.select(artistId); // 기존 데이터 조회

        // 기본 정보 업데이트
        artist.setName(request.name());
        artist.setDescription(request.description());
        artist.setDebutDate(request.debutDate());  // LocalDate or String → DTO에 맞게
        artist.setInsta(request.insta());
        artist.setYoutube(request.youtube());

        artistService.update(artist);

        return ResponseEntity.ok(Map.of("result", "아티스트 수정 성공"));
    }

    // 아티스트 삭제
    @DeleteMapping("/artists/{artistId}")
    public ResponseEntity<?> deleteArtist(@PathVariable int artistId) {
        artistService.delete(artistId);

        return ResponseEntity.ok(Map.of("result","아티스트 삭제 성공"));
    }

    // 특정 파트너의 아티스트 조회 (쿼리 파라미터 방식)
    @GetMapping(value = "/artists", params = "partnerId")
    public List<ArtistResponse> getArtistsByPartner(@RequestParam int partnerId) {
        return artistService.findByPartnerId(partnerId)
                .stream()
                .map(ArtistResponse::from)
                .toList();
    }
}
