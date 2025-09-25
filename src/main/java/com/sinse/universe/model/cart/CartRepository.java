package com.sinse.universe.model.cart;

import com.sinse.universe.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    public List<Cart> findByUser_Id(Integer userId);
}
