package com.sinse.universe.controller;

import com.sinse.universe.domain.Order;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.OrderResponse;
import com.sinse.universe.model.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ent/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 한 소속사의 주문 목록 요청
    @GetMapping("/partner/{partnerId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByPartner(@PathVariable int partnerId) {
        return ApiResponse.success("한 소속사의 주문 목록 요청", orderService.selectByPartnerId(partnerId));
    }

    // 한 아티스트의 주문 목록 요청
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByArtist(@PathVariable int artistId) {
        return ApiResponse.success("한 아티스트의 주문 목록 요청", orderService.selectByArtistId(artistId));
    }








/*
    // 한 유저의 주문 목록 요청... 은 유저 페이지에서 써야지...
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(@PathVariable int userId) {
        log.debug(orderService.selectByUserId(userId).toString());
        return ApiResponse.success("주문 목록 요청", orderService.selectByUserId(userId));
    }

    // 주문 상세 목록 요청... 은 관리자 페이지에서 써야지...
    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(@PathVariable int id) {
        return ApiResponse.success("주문 상세 요청", orderService.select(id));
    }
*/

}
