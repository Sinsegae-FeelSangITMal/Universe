package com.sinse.universe.model.order;

import com.sinse.universe.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findByUserId(int userId);
}