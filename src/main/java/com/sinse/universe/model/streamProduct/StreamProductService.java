package com.sinse.universe.model.streamProduct;

import com.sinse.universe.dto.request.StreamProductRequest;
import com.sinse.universe.dto.response.StreamProductResponse;

import java.util.List;

public interface StreamProductService {
    // 전체 조회
    List<StreamProductResponse> selectAll();

    // 단일 조회
    StreamProductResponse select(int id);

    // 특정 스트림에 연결된 상품 조회
    List<StreamProductResponse> findByStreamId(int streamId);

    // 등록
    void regist(StreamProductRequest request);

    // 수정
    int update(StreamProductRequest request);

    // 단일 삭제
    void delete(int id);

    // 특정 스트림의 모든 상품 삭제
    void deleteByStreamId(int streamId);

    // 여러 상품 연결
    void addProducts(int streamId, List<Integer> productIds);

    // 여러 상품 해제
    void removeProductsByStream(int streamId);
}
