package com.sinse.universe.model.product;

import com.sinse.universe.domain.*;
import com.sinse.universe.dto.request.ProductRegistRequest;
import com.sinse.universe.dto.response.ProductResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.category.CategoryRepository;
import com.sinse.universe.util.UploadManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
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

    public ProductServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository,
                              CategoryRepository categoryRepository, ArtistRepository artistRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.artistRepository = artistRepository;
    }

    @Transactional
    public int regist(ProductRegistRequest req,
                      MultipartFile mainImage,
                      List<MultipartFile> detailImages) {

        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.categoryId()));
        Artist artist = artistRepository.findById(req.artistId())
                .orElseThrow(() -> new IllegalArgumentException("Artist not found: " + req.artistId()));

        // Request객체 -> Domain객체
        Product product = toEntity(req, category, artist);
        productRepository.saveAndFlush(product); // PK 즉시 발급

        // 롤백시 삭제할 파일 경로 추적
        List<Path> uploadedPaths = new ArrayList<>();

        try {
            // ===== 메인 이미지 =====
            if (mainImage != null && !mainImage.isEmpty()) {
                String mainDir = productMainDir + "/p" + product.getId(); // 예: C://upload/product/main/p123
                String mainFilename = UploadManager.storeAndReturnName(mainImage, mainDir);

                // 삭제 추적
                uploadedPaths.add(Paths.get(mainDir).resolve(mainFilename));

                // URL 구성:C://upload/product/main/p{productId}/{filename}
                String mainUrl = "C://upload/product/main/p" + product.getId() + "/" + mainFilename;

                ProductImage mainImg = new ProductImage();
                mainImg.setProduct(product);
                mainImg.setRole(ProductImage.Role.MAIN);
                mainImg.setOriginalName(mainImage.getOriginalFilename());
                mainImg.setMimeType(mainImage.getContentType());
                mainImg.setSize(mainImage.getSize());
                mainImg.setUrl(mainUrl);

                productImageRepository.saveAndFlush(mainImg);
            }

            // ===== 상세 이미지들 =====
            if (detailImages != null && !detailImages.isEmpty()) {
                String detailDir = productDetailDir + "/p" + product.getId(); // 예: C://upload/product/detail/p123

                List<ProductImage> details = new ArrayList<>();
                for (MultipartFile file : detailImages) {
                    if (file == null || file.isEmpty()) continue;

                    String filename = UploadManager.storeAndReturnName(file, detailDir);

                    // 삭제 추적
                    uploadedPaths.add(Paths.get(detailDir).resolve(filename));

                    // URL 구성:C://upload/product/main/p{productId}/{filename}s
                    String url = "C://upload/product/main/p" + product.getId() + "/" + filename;

                    ProductImage pi = new ProductImage();
                    pi.setProduct(product);
                    pi.setRole(ProductImage.Role.DETAIL);
                    pi.setOriginalName(file.getOriginalFilename());
                    pi.setMimeType(file.getContentType());
                    pi.setSize(file.getSize());
                    pi.setUrl(url);

                    details.add(pi);
                }
                if (!details.isEmpty()) {
                    productImageRepository.saveAllAndFlush(details);
                }
            }

            return product.getId();

        } catch (IOException e) {
            // 디스크 롤백
            for (Path p : uploadedPaths) {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException ex) {
                    log.warn("파일 삭제 실패: {}", p, ex);
                }
            }
            throw new RuntimeException("File save failed", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getProductDetail(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 이미지 로드 (원하는 방식으로 골라 쓰세요)
        // 1) productId로
        List<ProductImage> images = productImageRepository.findByProductId(productId);

        // 메인 1장
        String mainImageUrl = images.stream()
                .filter(img -> img.getRole() == ProductImage.Role.MAIN)
                .findFirst()
                .map(ProductImage::getUrl)
                .orElse(null);

        // 상세 N장
        List<ProductResponse.ImageDto> detailImages = images.stream()
                .filter(img -> img.getRole() == ProductImage.Role.DETAIL)
                .map(img -> new ProductResponse.ImageDto(img.getId(), img.getUrl()))
                .toList();

        // DTO 변환 (from 오버로드 사용)
        return ProductResponse.from(product, mainImageUrl, detailImages);
    }


    @Override
    public void update(ProductRegistRequest req,
                       MultipartFile mainImage,
                       List<MultipartFile> detailImages){

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
