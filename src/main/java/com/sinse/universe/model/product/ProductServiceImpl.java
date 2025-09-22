package com.sinse.universe.model.product;

import com.sinse.universe.domain.*;
import com.sinse.universe.dto.request.ProductRegistRequest;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.category.CategoryRepository;
import com.sinse.universe.util.UploadManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${file.upload.product.main}")
    private String productMainDir;
    @Value("${file.upload.product.detail}")
    private String productDetailDir;

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final ArtistRepository artistRepository;
    private final UploadManager uploadManager;

    public ProductServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository,
                              CategoryRepository categoryRepository, ArtistRepository artistRepository, UploadManager uploadManager) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.artistRepository = artistRepository;
        this.uploadManager = uploadManager;
    }
    @Transactional
    public int regist(ProductRegistRequest req,
                      MultipartFile mainImage,
                      List<MultipartFile> detailImages) {

        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.categoryId()));
        Artist artist = artistRepository.findById(req.artistId())
                .orElseThrow(() -> new IllegalArgumentException("Artist not found: " + req.artistId()));

        Product product = toEntity(req, category, artist);
        productRepository.saveAndFlush(product); // PK 즉시 발급 보장

        try {
            // 메인 이미지
            if (mainImage != null && !mainImage.isEmpty()) {
                ProductImage mainImg = uploadManager.store(mainImage, product, productMainDir, ProductImage.Role.MAIN);
                productImageRepository.saveAndFlush(mainImg);
                // (DB 기본값/ON UPDATE 전략이라면) entityManager.refresh(mainImg);
            }

            // 상세 이미지
            if (detailImages != null && !detailImages.isEmpty()) {
                List<ProductImage> details = new ArrayList<>();
                for (MultipartFile file : detailImages) {
                    if (file == null || file.isEmpty()) continue;
                    ProductImage pi = uploadManager.store(file, product, productDetailDir, ProductImage.Role.DETAIL);
                    details.add(pi);
                }
                if (!details.isEmpty()) {
                    productImageRepository.saveAllAndFlush(details);
                    // (DB 기본값/ON UPDATE 전략이라면) details.forEach(entityManager::refresh);
                }
            }

            return product.getId();
        } catch (IOException e) {
            // TODO: 여기서 이미 디스크에 쓴 파일 경로를 추적해 삭제(롤백)하면 더 안전함
            throw new RuntimeException("File save failed", e);
        }
    }

    @Override
    public void update(Product product) {

    }

    @Override
    public void delete(int productId) {

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

    // Request객체 -> Entity로 변환
    private Product toEntity(ProductRegistRequest r, Category c, Artist a) {
        Product p = new Product();
        p.setName(r.productName());
        p.setDescription(r.detail());
        p.setPrice(r.price());
        p.setOpenDate(r.salesOpenAt());
        p.setFanOnly(r.fanLimited());
        p.setStockQuantity(r.initialStock());
        p.setLimitPerUser(r.purchaseLimit());
        p.setStatus(Product.ProductStatus.active);
        p.setCategory(c);
        p.setArtist(a);
        return p;
    }
}
