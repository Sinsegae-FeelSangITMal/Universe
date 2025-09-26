package com.sinse.universe.model.product;

import com.sinse.universe.domain.Product;
import com.sinse.universe.dto.response.ProductMainResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductUserService {

    public Page<Product> pageNewProducts(Pageable pageable);
}
