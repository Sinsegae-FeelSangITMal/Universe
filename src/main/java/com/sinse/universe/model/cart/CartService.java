package com.sinse.universe.model.cart;

import com.sinse.universe.dto.response.CartResponse;

import java.util.List;

public interface CartService {
    public List<CartResponse> getCarts(int userId);
}
