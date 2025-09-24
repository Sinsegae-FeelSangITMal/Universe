package com.sinse.universe.model.product;

import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    public List<ProductImage> findByProductId(Integer productId);
    public List<ProductImage> findByProductAndRole(Product product, ProductImage.Role role);
}
