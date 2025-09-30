package com.sinse.universe.controller;

import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.CartResponse;
import com.sinse.universe.dto.response.OrderForEntResponse;
import com.sinse.universe.dto.response.PartnerArtistResponse;
import com.sinse.universe.model.artist.ArtistService;
import com.sinse.universe.model.cart.CartService;
import com.sinse.universe.model.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;
    private final ArtistService artistService;
    public OrderController(OrderService orderService, ArtistService artistService) {
        this.orderService = orderService;
        this.artistService = artistService;
    }

    // 한 소속사의 주문 목록 요청
    @GetMapping("/ent/orders/partner/{partnerId}")
    public ResponseEntity<ApiResponse<List<OrderForEntResponse>>> getOrdersByPartner(@PathVariable int partnerId) {
        return ApiResponse.success("한 소속사의 주문 목록 요청", orderService.getListByPartnerId(partnerId));
    }

    // 한 아티스트의 주문 목록 요청
    @GetMapping("/ent/orders/artist/{artistId}")
    public ResponseEntity<ApiResponse<List<OrderForEntResponse>>> getOrdersByArtist(@PathVariable int artistId) {
        return ApiResponse.success("한 아티스트의 주문 목록 요청", orderService.getListByArtistId(artistId));
    }

    // 한 유저의 주문 목록 요청
    @GetMapping("/ent/orders/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderForEntResponse>>> getOrdersByUser(@PathVariable int userId) {
        return ApiResponse.success("한 유저의 주문 목록 요청", orderService.getListByUserId(userId));
    }

    // 주문 상세 목록 요청... 은 관리자 페이지에서 써야지...
    @GetMapping("/ent/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderForEntResponse>> getOrderDetail(@PathVariable int orderId) {
        return ApiResponse.success("주문 상세 요청", orderService.getDetail(orderId));
    }


}
