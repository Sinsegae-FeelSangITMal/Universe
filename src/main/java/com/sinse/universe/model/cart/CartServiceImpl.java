package com.sinse.universe.model.cart;

import com.sinse.universe.dto.response.CartResponse;
import com.sinse.universe.dto.response.OrderForEntResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public List<CartResponse> getCarts(int userId) {
        return cartRepository.findByUser_Id(userId).stream()
                .map(CartResponse::from)
                .toList();
    }
}
