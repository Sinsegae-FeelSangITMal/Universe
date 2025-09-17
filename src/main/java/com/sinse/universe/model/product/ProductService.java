package com.sinse.universe.model.product;

import com.sinse.universe.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    public List<Product> selectAll();
    public Product select(int productId);
    public void regist(Product product);
    public void update(Product product);
    public void delete(int productId);

    // 한 아티스트의 모든 상품 조회
    public Page<Product> pageByArtist(Integer artistId, Pageable pageable);
}
