package com.sinse.universe.model.order;

import com.sinse.universe.dto.response.OrderForEntResponse;

import java.util.List;

public interface OrderService {
    // 유저의 주문 목록 요청
    public List<OrderForEntResponse> getListByUserId(int userId);
    // 소속사의 상품이 포함된 주문 목록 요청
    public List<OrderForEntResponse> getListByPartnerId(int partnerId);
    // 아티스트의 상품이 포함된 주문 목록 요청
    public List<OrderForEntResponse> getListByArtistId(int artistId);
    // 주문 상세 요청
    public OrderForEntResponse getDetail(int orderId);
}
