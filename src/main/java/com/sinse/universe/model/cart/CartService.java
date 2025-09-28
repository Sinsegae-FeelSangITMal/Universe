package com.sinse.universe.model.cart;

import com.sinse.universe.dto.request.CartAddRequest;
import com.sinse.universe.dto.response.CartResponse;

import java.util.List;

public interface CartService {
    // 장바구니 목록 불러오기
    public List<CartResponse> getCarts(int userId);

    // 장바구니에 추가 - 1 : 수량 누적
    public void addCart(CartAddRequest request);

    // 장바구니에 추가 - 2 : 수량 덮어쓰기
    public void updateCart(int cartId, int qty);
        
    // 장바구니에서 삭제
    public void delCart(int cartId);

}
