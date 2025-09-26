package com.sinse.universe.controller;

import com.sinse.universe.domain.Product;
import com.sinse.universe.dto.request.ProductRegistRequest;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.ProductDetailResponse;
import com.sinse.universe.dto.response.ProductResponse;
import com.sinse.universe.model.product.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/ent/products/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductDetail(@PathVariable Integer productId) {
        ProductDetailResponse pdr = productService.getProductDetail(productId);
        log.debug(pdr.toString());

        return ApiResponse.success("조회 성공", pdr);
    }

    /**
     * 상품 페이지 조회 (아티스트별 필터 가능)
     * 예: GET  /products?page=0&size=20&sort=id,desc
     * 예: GET /products?artistId=3&page=0&size=10
     */
    @GetMapping("/ent/products")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProducts(
            @RequestParam(required = false) Integer artistId,
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

        return ApiResponse.success(("ProductDtoPage 반환 성공"), dtoPage);
    }

    @PostMapping(value = "/ent/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, Object>>> registerProduct(
            @ModelAttribute @Valid ProductRegistRequest productRegistRequest,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestPart(value = "detailImages", required = false) List<MultipartFile> detailImages
    ) {
        log.debug(productRegistRequest.toString());
        if (detailImages == null) detailImages = List.of();
        int id = productService.regist(productRegistRequest, mainImage, detailImages);

        // Location을 쓸 계획이 없더라도 201은 유지
        return ApiResponse.created("등록 성공", Map.of("id", id));
    }

    @PutMapping(
            value = "/ent/products/{productId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateProduct(
            @PathVariable int productId,
            @ModelAttribute @Valid ProductRegistRequest productRegistRequest,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestPart(value = "detailImages", required = false) List<MultipartFile> detailImages,
            @RequestParam(value = "deleteDetailImageIds", required = false) List<Integer> deleteDetailImageIds
    ) {
        int id = productService.update(productId, productRegistRequest, mainImage, detailImages, deleteDetailImageIds);
        return ApiResponse.success("수정 성공", Map.of("id", id)); // 200 OK
    }
}
