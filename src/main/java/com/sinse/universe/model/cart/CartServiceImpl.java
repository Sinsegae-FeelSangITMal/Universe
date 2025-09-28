package com.sinse.universe.model.cart;

import com.sinse.universe.domain.Cart;
import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.User;
import com.sinse.universe.dto.request.CartAddRequest;
import com.sinse.universe.dto.response.CartResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.product.ProductRepository;
import com.sinse.universe.model.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    // 장바구니 목록 블러오기
    @Override
    public List<CartResponse> getCarts(int userId) {
        return cartRepository.findByUser_Id(userId).stream()
                .map(CartResponse::from)
                .toList();
    }

    // 장바구니에 추가 - 1
    // 상품 목록에서 담기 → 누적
    @Override
    public void addCart(CartAddRequest request) {
        User user = userRepository.getReferenceById(request.userId());
        Product product = productRepository.getReferenceById(request.productId());

        Cart cart = cartRepository.findByUser_IdAndProduct_Id(request.userId(), request.productId())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    c.setProduct(product);
                    c.setQty(0); // 초기화
                    return c;
                });

        cart.setQty(cart.getQty() + request.qty()); // 누적
        cartRepository.save(cart);
    }

    // 장바구니에 추가 - 2
    // 장바구니에서 수정 → 덮어쓰기
    @Override
    public void updateCart(int cartId, int qty) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));
        cart.setQty(qty); // 덮어쓰기
        cartRepository.save(cart);
    }

    // 장바구니에서 삭제
    @Override
    public void delCart(int cartId) {
        cartRepository.deleteById(cartId);
    }

}
