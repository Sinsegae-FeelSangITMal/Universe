package com.sinse.universe.model.streamProduct;

import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.Stream;
import com.sinse.universe.domain.StreamProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StreamProductRepository extends JpaRepository<StreamProduct, Integer> {
    @Modifying
    @Query("DELETE FROM StreamProduct sp WHERE sp.stream.id = :streamId")
    void deleteByStreamId(int streamId);

    // 특정 Stream 에 연결된 상품 목록 조회
    @Query("SELECT sp.product FROM StreamProduct sp WHERE sp.stream.id = :streamId")
    List<Product> findProductsByStreamId(int streamId);

    // 특정 Stream 과 특정 Product 조합 존재 여부 확인
    boolean existsByStreamAndProduct(Stream stream, Product product);

    // 특정 Stream 의 StreamProduct 전체 조회 (필요 시)
    List<StreamProduct> findByStreamId(int streamId);
}
