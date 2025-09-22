package com.sinse.universe.controller;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Partner;
import com.sinse.universe.domain.Product;
import com.sinse.universe.dto.request.ProductRequest;
import com.sinse.universe.dto.response.ArtistDetailResponse;
import com.sinse.universe.dto.response.ArtistResponse;
import com.sinse.universe.dto.response.ProductResponse;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.artist.ArtistService;
import com.sinse.universe.model.partner.PartnerRepository;
import com.sinse.universe.model.product.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    
    // 전체 아티스트
    @GetMapping("/artists")
    public List<ArtistResponse> getArtists() {
        return artistRepository.findAll()
                .stream()
                .map(ArtistResponse::from)
                .toList();
    }

    // 상세 아티스트
    @GetMapping("/artists/{artistId}")
        public Artist getArtist(@PathVariable int artistId){
            return artistService.select(artistId);
    }

        // 아티스트 등록
//        @PostMapping("/artists")
//        public ResponseEntity addArtist(@RequestBody Artist artist) {
//            artistService.regist(artist);
//            return ResponseEntity.ok(Map.of("result", "아티스트 등록 성공"));
//        }

    public record ArtistRequest(
            String name,
            String description,
            Integer partnerId
    ) {}

    @PostMapping("/artists")
    public ResponseEntity<?> addArtist(@RequestBody ArtistRequest request) {
        Partner partner = partnerRepository.findById(request.partnerId())
                .orElseThrow(() -> new RuntimeException("Partner not found"));
        Artist artist = new Artist();
        artist.setName(request.name());
        artist.setDescription(request.description());
        artist.setPartner(partner);
        artistRepository.save(artist);
        return ResponseEntity.ok(Map.of("result","성공"));
    }

        // 아티스트 수정
        @PutMapping("/artists/{artistId}")
        public ResponseEntity updateArtist(@RequestBody Artist artist, @PathVariable int artistId) {
            artistService.update(artist);
            return ResponseEntity.ok(Map.of("result", "수정 성공"));
        }

        // 아티스트 삭제
        @DeleteMapping("/artists/{artistId}")
        public ResponseEntity deleteArtist(@PathVariable int artistId) {
            artistService.delete(artistId);
            return ResponseEntity.ok(Map.of("result","삭제 성공"));
        }
    }
