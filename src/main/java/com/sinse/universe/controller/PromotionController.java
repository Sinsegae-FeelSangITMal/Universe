package com.sinse.universe.controller;

import com.sinse.universe.domain.Promotion;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.PromotionResponse;
import com.sinse.universe.model.promotion.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ent")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    // 아티스트별 프로모션 조회
    @GetMapping("/artists/{artistId}/promotions")
    public ResponseEntity<ApiResponse<List<PromotionResponse>>> getPromotionsByArtist(
            @PathVariable int artistId
    ) {
        List<Promotion> promotions = promotionService.findByArtistId(artistId);

        List<PromotionResponse> dtoList = promotions.stream()
                .map(PromotionResponse::from)
                .toList();

        return ApiResponse.success("아티스트별 프로모션 조회 성공", dtoList);
    }

    // 단일 프로모션 조회
    @GetMapping("/promotions/{promotionId}")
    public ResponseEntity<ApiResponse<PromotionResponse>> getPromotion(@PathVariable int promotionId) {
        Promotion promotion = promotionService.select(promotionId);
        return ApiResponse.success("조회 성공", PromotionResponse.from(promotion));
    }

    // 프로모션 등록
    @PostMapping(value = "/promotions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, Object>>> registPromotion(
            @ModelAttribute Promotion promotion, // DTO 대신 Promotion 직접 받음
            @RequestPart(value = "img", required = false) MultipartFile img
    ) {
        log.debug("Promotion 등록 요청: {}", promotion);

        // 이미지 파일 처리 로직 필요시 service에서 구현
        promotionService.regist(promotion);

        return ApiResponse.created("등록 성공", Map.of("id", promotion.getId()));
    }

    // 프로모션 수정
    @PutMapping(value = "/promotions/{promotionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, Object>>> updatePromotion(
            @PathVariable int promotionId,
            @ModelAttribute Promotion promotion,
            @RequestPart(value = "img", required = false) MultipartFile img
    ) {
        log.debug("Promotion 수정 요청: {}", promotion);

        promotion.setId(promotionId);
        promotionService.update(promotion);

        return ApiResponse.success("수정 성공", Map.of("id", promotionId));
    }

    // 프로모션 삭제
    @DeleteMapping("/promotions/{promotionId}")
    public ResponseEntity<ApiResponse<Void>> deletePromotion(@PathVariable int promotionId) {
        promotionService.delete(promotionId);
        return ApiResponse.success("삭제 성공", null);
    }
}
