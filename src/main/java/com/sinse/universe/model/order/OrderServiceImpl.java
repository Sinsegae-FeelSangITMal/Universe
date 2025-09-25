package com.sinse.universe.model.order;

import com.sinse.universe.dto.response.OrderForEntResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 한 유저의 주문 목록 가져오기
    @Override
    public List<OrderForEntResponse> getListByUserId(int userId) {
        return orderRepository.findByUser_Id(userId).stream()
                .map(OrderForEntResponse::from)
                .toList();
    }

    // 한 소속사의 아이템이 주문된 목록만 가져오기
    @Override
    public List<OrderForEntResponse> getListByPartnerId(int partnerId) {
        return orderRepository.findByPartnerId(partnerId).stream()
                .map(OrderForEntResponse::from)
                .toList();
    }

    // 한 아티스트의 아이템이 주문된 목록만 가져오기
    @Override
    public List<OrderForEntResponse> getListByArtistId(int artistId) {
        return orderRepository.findByArtistId(artistId).stream()
                .map(OrderForEntResponse::from)
                .toList();
    }

    @Override
    public OrderForEntResponse getDetail(int orderId) {
        return orderRepository.findById(orderId)
                .map(OrderForEntResponse::from)
                .orElse(null);
    }
}
