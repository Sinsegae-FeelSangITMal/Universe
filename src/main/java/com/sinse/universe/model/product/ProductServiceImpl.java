package com.sinse.universe.model.product;

import com.sinse.universe.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public Page<Product> pageByArtist(Integer artistId, Pageable pageable){
        Page<Integer> idPage = productRepository.pageIdsByArtist(artistId, pageable);
        List<Product> rows = idPage.getContent().isEmpty()
                ? List.of()
                : productRepository.findWithRefsByIds(idPage.getContent());
        // 원래 순서 보정
        Map<Integer,Integer> order = new HashMap<>();
        for (int i=0;i<idPage.getContent().size();i++) order.put(idPage.getContent().get(i), i);
        rows.sort(Comparator.comparing(p -> order.get(p.getId())));
        return new PageImpl<>(rows, pageable, idPage.getTotalElements());
    }
}
