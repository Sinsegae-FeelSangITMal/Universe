package com.sinse.universe.model.cart;

import com.sinse.universe.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    // 장바구니 목록 요청
    public List<Cart> findByUser_Id(int userId);
    // 데이터 존재 검증
    public Optional<Cart> findByUser_IdAndProduct_Id(int userId, int productId);
    // 장바구니에서 삭제
    public void deleteByUser_IdAndProduct_IdIn(int userId, List<Integer> productIds);
}
