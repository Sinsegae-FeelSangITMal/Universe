package com.sinse.universe.model.order;

import com.sinse.universe.domain.Order;
import com.sinse.universe.dto.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderResponse> selectByPartnerId(int partnerId) {
        return orderRepository.findOrdersByPartnerId(partnerId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Override
    public List<OrderResponse> selectByArtistId(int artistId) {
        return orderRepository.findOrdersByArtistId(artistId).stream()
                .map(OrderResponse::from)
                .toList();
    }
}
