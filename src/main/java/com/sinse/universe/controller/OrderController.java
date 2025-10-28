package com.sinse.universe.controller;

import com.sinse.universe.domain.Order;
import com.sinse.universe.dto.request.OrderSubmitRequest;
import com.sinse.universe.dto.response.*;
import com.sinse.universe.model.order.OrderService;
import com.sinse.universe.model.payment.KakaoReadyResponse;
import com.sinse.universe.model.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    // 판매자 페이지) 한 소속사의 주문 목록 요청
    @GetMapping("/ent/orders/partner/{partnerId}")
    public ResponseEntity<ApiResponse<List<OrderForEntResponse>>> getOrdersByPartner(@PathVariable int partnerId) {
        return ApiResponse.success("한 소속사의 주문 목록 요청", orderService.getListByPartnerId(partnerId));
    }

    // 판매자 페이지) 한 아티스트의 주문 목록 요청
    @GetMapping("/ent/orders/artist/{artistId}")
    public ResponseEntity<ApiResponse<List<OrderForEntResponse>>> getOrdersByArtist(@PathVariable int artistId) {
        return ApiResponse.success("한 아티스트의 주문 목록 요청", orderService.getListByArtistId(artistId));
    }

    // 유저 페이지) 한 유저의 주문 목록 요청
    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderForUserResponse>>> getOrdersByUser(@PathVariable int userId) {
        return ApiResponse.success("한 유저의 주문 목록 요청", orderService.getListByUserId(userId));
    }

    // 유저 페이지) 주문서 받기  ** 임시, 결제 서버 생성 시 옮겨야 함 or 서비스 처리**
    @PostMapping("/orders")
    public ResponseEntity<?> submitOrder (@RequestBody OrderSubmitRequest request) {
        Order order = orderService.submitOrder(request);
        log.debug("order={}", order);

        KakaoReadyResponse kakaoReadyResponse = paymentService.requestKakaoPayReady(order);
        log.debug("kakaoReadyResponse={}", kakaoReadyResponse);

        return ApiResponse.success("결제 요청", Map.of(
                "orderId", Integer.toString(order.getId()),
                "redirectUrl", kakaoReadyResponse.getNext_redirect_pc_url()
        ));
    }

    // 유저 페이지) 주문 상세 목록 요청
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderForUserResponse>> getOrderDetail(@PathVariable int orderId) {
        return ApiResponse.success("주문 상세 요청", orderService.getDetail(orderId));
    }
}
