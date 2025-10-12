package com.sinse.universe.model.product;

import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.ProductImage;
import com.sinse.universe.dto.response.ProductMainResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUserServiceImpl implements ProductUserService{

    private final ProductRepository productRepository;

    public ProductUserServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> pageNewProducts(Pageable pageable) {
        return productRepository.findLatestActiveWithMainExcludingMembership(pageable);
    }

    @Override
    public Page<Product> getProductsByArtist(Pageable pageable, int artistId) {
        return productRepository.findByArtistAndOptionalCategory(artistId, null, pageable);
    }

    @Override
    public Page<Product> getProductsByArtistAndCategory(Pageable pageable, int artistId, Integer categoryId) {
        return productRepository.findByArtistAndOptionalCategory(artistId, categoryId, pageable);
    }
}
