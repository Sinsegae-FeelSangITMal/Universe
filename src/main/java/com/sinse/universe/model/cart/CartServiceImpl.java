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

import static com.sinse.universe.enums.ErrorCode.CART_LIMIT;
import static com.sinse.universe.enums.ErrorCode.CART_NO_STOCK;

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
    public void addCart(CartAddRequest request) throws CustomException {
        // 유저와 상품 정보 가져오기
        User user = userRepository.getReferenceById(request.userId());
        Product product = productRepository.getReferenceById(request.productId());
        int limit = product.getLimitPerUser();      // 상품 개수 제한 가져오기

        // 유저와 상품 정보로 장바구니 정보 가져오기, 없으면 새로 데이터 세팅
        Cart cart = cartRepository.findByUser_IdAndProduct_Id(request.userId(), request.productId())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    c.setProduct(product);
                    c.setQty(0); // 초기화
                    return c;
                });

        // 1. 상품 개수 제한이 없거나, 2. 요청한 개수가 유저 당 개수 제한보다 크지 않으면 요청한 개수 수행
        if (limit == -1
                || (limit > -1 && limit >= cart.getQty() + request.qty())) {
            cart.setQty(cart.getQty() + request.qty());
            cartRepository.save(cart);
        } else {
            // 제한이 있을 경우 제한된 수량까지만 담기
            cart.setQty(limit);
            cartRepository.save(cart);
            throw new CustomException(CART_LIMIT);
        }
    }

    // 장바구니에 추가 - 2
    // 장바구니에서 수정 → 덮어쓰기
    @Override
    public void updateCart(int cartId, int qty) throws CustomException {
        // 장바구니 정보 가져오기, 없으면 예외 처리
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        Product product = cart.getProduct();

        if (product.getStockQuantity() >= qty) {
            cart.setQty(qty); // 덮어쓰기
            cartRepository.save(cart);
        }
    }

    // 장바구니에서 삭제
    @Override
    public void delCart(int cartId) {
        cartRepository.deleteById(cartId);
    }

}
