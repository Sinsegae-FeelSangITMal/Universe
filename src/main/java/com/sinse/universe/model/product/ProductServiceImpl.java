package com.sinse.universe.model.product;

import com.sinse.universe.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> selectAll() {
        return productRepository.findAll();
    }

    @Override
    public Product select(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public void regist(Product product) {
        productRepository.save(product);
    }

    @Override
    public void update(Product product) {
        productRepository.save(product);
    }

    @Override
    public void delete(int productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> selectAllByArtist(int artistId) {
        return productRepository.findAllByArtist(artistId);
    }
}
