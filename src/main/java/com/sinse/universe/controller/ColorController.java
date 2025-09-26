package com.sinse.universe.controller;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Color;
import com.sinse.universe.dto.request.ColorRequest;
import com.sinse.universe.dto.response.ColorResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.color.ColorRepository;
import com.sinse.universe.model.color.ColorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ColorController {

    private final ColorService colorService;
    private final ColorRepository colorRepository;
    private final ArtistRepository artistRepository;

    public ColorController(ColorService colorService, ColorRepository colorRepository, ArtistRepository artistRepository) {
        this.colorService = colorService;
        this.colorRepository = colorRepository;
        this.artistRepository = artistRepository;
    }

    @GetMapping("/ent/colors")
    public List<ColorResponse> getColors() {
        return colorRepository.findAll()
                .stream()
                .map(ColorResponse::from)
                .toList();
    }

    // 특정 색상 조회
    @GetMapping("/ent/colors/{colorId}")
    public ColorResponse getColor(@PathVariable int colorId) {
        return ColorResponse.from(colorService.select(colorId));
    }

    // 색상 등록
    @PostMapping("/ent/colors")
    public ResponseEntity<?> registColor(@RequestBody ColorRequest request) {
        Artist artist = artistRepository.findById(request.artistId())
                .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));
        
        Color color = new Color();
        color.setBgColor(request.bgColor());
        color.setArtist(artist);
        
        colorRepository.save(color);

        return ResponseEntity.ok(Map.of("result","아티스트 커스텀 페이지 컬러 등록 성공"));
    }

    // 색상 수정
    @PutMapping("/ent/colors/{colorId}")
    public ResponseEntity<?> updateColor(@PathVariable int colorId, @RequestBody ColorRequest request) {
        Color color = colorService.select(colorId); // 기존 데이터 조회

        // 기본 정보 업데이트
        color.setId(colorId);
        color.setBgColor(request.bgColor());

        colorService.update(color);

        return ResponseEntity.ok(Map.of("result", "컬러 수정 성공"));
    }

    // 색상 삭제
    @DeleteMapping("/ent/colors/{colorId}")
    public ResponseEntity<?> deleteColor(@PathVariable int colorId) {
        colorService.delete(colorId);
        return ResponseEntity.ok(Map.of("result", "컬러 삭제 성공"));
    }

    // 특정 아티스트의 컬러 조회
    @GetMapping("/ent/artists/{artistId}/colors")
    public ResponseEntity<?> getColorByArtist(@PathVariable int artistId) {
        Color color = colorService.findByArtistId(artistId);
        if (color == null) {
            return ResponseEntity.ok().body(null); // ✅ 200 OK + null
        }
        return ResponseEntity.ok(ColorResponse.from(color));
    }
}
