package com.sinse.universe.controller;

import com.sinse.universe.dto.request.CartAddRequest;
import com.sinse.universe.dto.request.CartUpdateRequest;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.CartResponse;
import com.sinse.universe.model.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
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

    // 장바구니 항목 추가
    @PostMapping("/")
    public ResponseEntity<?> addCart(@RequestBody CartAddRequest request) {
        cartService.addCart(request);
        return ApiResponse.success("장바구니에 추가");
    }

    // 장바구니 항목 수정
    @PutMapping("/{cartId}")
    public ResponseEntity<?> updateCart(@PathVariable int cartId, @RequestBody CartUpdateRequest request) {
        cartService.updateCart(cartId, request.qty());
        return ApiResponse.success("장바구니 수정");
    }

    // 장바구니 항목 삭제
    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> delCart(@PathVariable int cartId) {
        cartService.delCart(cartId);
        return ApiResponse.success("장바구니에서 삭제");
    }

}
