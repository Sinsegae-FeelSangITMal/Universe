package com.sinse.universe.model.order;

import com.sinse.universe.domain.Order;
import com.sinse.universe.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    public List<OrderResponse> selectByPartnerId(int partnerId);
    public List<OrderResponse> selectByArtistId(int artistId);
}
