package com.sinse.universe.model.product;

import com.sinse.universe.domain.Product;
import com.sinse.universe.dto.request.ProductRegistRequest;
import com.sinse.universe.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    public int regist(ProductRegistRequest req,
                       MultipartFile mainImage,
                       List<MultipartFile> detailImages);
    public ProductResponse getProductDetail(Integer productId);
    public void update(ProductRegistRequest req,
                       MultipartFile mainImage,
                       List<MultipartFile> detailImages);
    public void delete(int productId);

    // 한 아티스트의 모든 상품 조회
    public Page<Product> pageByArtist(Integer artistId, Pageable pageable);
}
