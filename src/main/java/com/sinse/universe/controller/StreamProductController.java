package com.sinse.universe.controller;

import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.StreamProduct;
import com.sinse.universe.dto.request.StreamProductRequest;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.StreamProductResponse;
import com.sinse.universe.model.streamProduct.StreamProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ent/stream-products")
public class StreamProductController {

    private final StreamProductService streamProductService;

    public StreamProductController(StreamProductService streamProductService) {
        this.streamProductService = streamProductService;
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<StreamProductResponse>>> getAllStreamProducts() {
        List<StreamProductResponse> list = streamProductService.selectAll();
        return ApiResponse.success("라이브 상품 전체 조회 성공", list);
    }

    // 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StreamProductResponse>> getStreamProduct(@PathVariable int id) {
        StreamProductResponse sp = streamProductService.select(id);
        return ApiResponse.success("라이브 상품 조회 성공", sp);
    }

    // 특정 라이브에 연결된 상품 조회
    @GetMapping(params = "streamId")
    public ResponseEntity<ApiResponse<List<StreamProductResponse>>> getStreamProductsByStream(
            @RequestParam int streamId) {
        return ApiResponse.success("특정 라이브 상품 조회 성공", streamProductService.findByStreamId(streamId));
    }

    // 등록
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addStreamProduct(@RequestBody StreamProductRequest request) {
        streamProductService.regist(request);
        return ApiResponse.success("스트림 상품 등록 성공", null);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateStreamProduct(
            @PathVariable int id,
            @RequestBody StreamProductRequest request
    ) {
        request.setId(id);
        int updatedId = streamProductService.update(request);
        return ApiResponse.success("라이브 상품 수정 성공", Map.of("id", updatedId));
    }

    // 단일 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteStreamProduct(@PathVariable int id) {
        streamProductService.delete(id);
        return ApiResponse.success("라이브 상품 삭제 성공", Map.of("id", id));
    }

    // 특정 스트림 전체 상품 삭제
    @DeleteMapping("/by-stream/{streamId}")
    public ResponseEntity<ApiResponse<Void>> deleteStreamProducts(@PathVariable int streamId) {
        streamProductService.deleteByStreamId(streamId);
        return ApiResponse.success("연결된 상품 삭제 성공", null);
    }

    // 여러 상품 연결
    @PostMapping("/{streamId}")
    public ResponseEntity<ApiResponse<Void>> addProducts(
            @PathVariable int streamId,
            @RequestBody List<Integer> productIds) {
        streamProductService.addProducts(streamId, productIds);
        return ApiResponse.success("상품 연결 성공", null);
    }

    // 여러 상품 해제
    @DeleteMapping("/remove/{streamId}")
    public ResponseEntity<ApiResponse<Void>> removeProducts(@PathVariable int streamId) {
        streamProductService.removeProductsByStream(streamId);
        return ApiResponse.success("상품 연결 해제 성공", null);
    }
}