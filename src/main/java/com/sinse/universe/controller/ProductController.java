package com.sinse.universe.controller;

import com.sinse.universe.domain.Product;
import com.sinse.universe.dto.request.ProductRegistRequest;
import com.sinse.universe.dto.response.ProductResponse;
import com.sinse.universe.model.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 상품 페이지 조회 (아티스트별 필터 가능)
     * 예: GET  /products?page=0&size=20&sort=id,desc
     * 예: GET /products?artistId=3&page=0&size=10
     */
    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(required = false) Integer artistId,
            // 기본 정렬을 지정하고 싶다면 @PageableDefault 사용
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        // 서비스는 Page<Product> 반환
        Page<Product> page = productService.pageByArtist(artistId, pageable);

        // 엔티티 → DTO 변환 (한 페이지의 content만 매핑)
        List<ProductResponse> content = page.getContent().stream()
                .map(ProductResponse::from)
                .toList();

        Page<ProductResponse> dtoPage = new PageImpl<>(content, pageable, page.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping(value = "/products", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerProduct(@ModelAttribute ProductRegistRequest productRegistRequest,
                                             @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
                                             @RequestPart(value = "detailImages", required = false) List<MultipartFile> detailImages) {

        log.debug("products regist 호출");
        log.debug(productRegistRequest.toString());
        log.debug("대표이미지: " + (mainImage != null ? mainImage.getOriginalFilename() : "없음"));
        log.debug("상세이미지 개수: " + (detailImages != null ? detailImages.size() : 0));

        return ResponseEntity.ok(Map.of("result", "등록 성공"));
    }

}
