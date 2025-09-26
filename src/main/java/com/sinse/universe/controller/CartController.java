package com.sinse.universe.controller;

import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.CartResponse;
import com.sinse.universe.model.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 한 유저의 장바구니 목록 요청
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartResponse>>> getCarts(@PathVariable int userId) {
        return ApiResponse.success("장바구니 목록 요청", cartService.getCarts(userId));
    }

    // 장바구니 항목 삭제
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<List<CartResponse>>> delCarts(@PathVariable int cartId) {
        return null;
    }

    // 장바구니 항목 추가
    @PostMapping("/{cartId}")
    public ResponseEntity<ApiResponse<List<CartResponse>>> addCarts(@PathVariable int cartId) {
        return null;
    }

}
