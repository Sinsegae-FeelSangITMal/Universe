package com.sinse.universe.model.product;

import com.sinse.universe.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public List<Product> findAllByArtist(int artistId);
}
