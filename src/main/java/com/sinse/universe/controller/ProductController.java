package com.sinse.universe.controller;

import com.sinse.universe.domain.Product;
import com.sinse.universe.model.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 한 아티스트의 모든 상품 조회
    @GetMapping("/products/{artistId}")
    public ResponseEntity<?> getProductsByArtist() {

        List<Product> productList = productService.selectAll();
        return ResponseEntity.ok(productList);
    }
}
