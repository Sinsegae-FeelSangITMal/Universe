package com.sinse.universe.model.order;

import com.sinse.universe.domain.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final JpaOrderRepository jpaOrderRepository;
    public OrderServiceImpl(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public List<Order> selectAll() {
        return jpaOrderRepository.findAll();
    }

    @Override
    public Order select(int orderId) {
        return jpaOrderRepository.findById(orderId).orElse(null);
    }

    @Override
    public void regist(Order order) {
        jpaOrderRepository.save(order);
    }

    @Override
    public void update(Order order) {
        Order obj = select(order.getId());
        if (obj != null)
            jpaOrderRepository.save(obj);
    }

    @Override
    public void delete(int orderId) {
        jpaOrderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> selectByUserId(int userId) {
        return jpaOrderRepository.findByUserId(userId);
    }
}
