package com.sinse.universe.model.order;

import com.sinse.universe.domain.Order;

import java.util.List;

public interface OrderService {
    public List<Order> selectAll();
    public Order select(int orderId);
    public void regist(Order order);
    public void update(Order order);
    public void delete(int orderId);
    public List<Order> selectByUserId(int userId);
}
