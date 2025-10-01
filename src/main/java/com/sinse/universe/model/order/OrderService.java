package com.sinse.universe.model.order;

import com.sinse.universe.dto.request.OrderSubmitRequest;
import com.sinse.universe.dto.response.OrderForEntResponse;
import com.sinse.universe.dto.response.OrderForUserResponse;

import java.util.List;

public interface OrderService {
    // 판매자 페이지) 소속사의 상품이 포함된 주문 목록 요청
    public List<OrderForEntResponse> getListByPartnerId(int partnerId);
    // 판매자 페이지) 아티스트의 상품이 포함된 주문 목록 요청
    public List<OrderForEntResponse> getListByArtistId(int artistId);
    // 유저 페이지) 유저의 주문 목록 요청
    public List<OrderForUserResponse> getListByUserId(int userId);
    // 유저 페이지) 주문 상세 요청
    public OrderForUserResponse getDetail(int orderId);
    // 유저 페이지) 주문 등록
    public int submitOrder(OrderSubmitRequest request);
}
