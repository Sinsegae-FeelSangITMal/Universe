package com.sinse.universe.controller;

import com.sinse.universe.domain.Order;
import com.sinse.universe.dto.response.ApiResponse;
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
@RequestMapping("/ent/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{userId}")
    public List<Order> getOrders(@PathVariable int userId) {
        return orderService.selectByUserId(userId);
    }

    @GetMapping("/detail/{id}")
    public Order getOrderDetail(@PathVariable int id) {
        return orderService.select(id);
    }
}
