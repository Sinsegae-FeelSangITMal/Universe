package com.sinse.universe.model.order;

import com.sinse.universe.dto.response.OrderForEntResponse;

import java.util.List;

public interface OrderService {
    public List<OrderForEntResponse> getListByUserId(int userId);
    public List<OrderForEntResponse> getListByPartnerId(int partnerId);
    public List<OrderForEntResponse> getListByArtistId(int artistId);
    public OrderForEntResponse getDetail(int orderId);
}
