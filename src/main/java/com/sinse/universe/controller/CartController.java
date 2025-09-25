package com.sinse.universe.controller;

import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.CartResponse;
import com.sinse.universe.model.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
