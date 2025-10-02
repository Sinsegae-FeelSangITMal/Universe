package com.sinse.universe.model.product;

import com.sinse.universe.domain.*;
import com.sinse.universe.dto.request.ProductRegistRequest;
import com.sinse.universe.dto.response.ProductDetailResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.category.CategoryRepository;
import com.sinse.universe.util.UploadManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Value("${upload.product-main-dir}")   // 예: C:/upload/product/main
    private String productMainDir;

    @Value("${upload.product-detail-dir}") // 예: C:/upload/product/detail
    private String productDetailDir;

    @Value("${upload.product-main-url}")   // 예: /images/product/main
    private String productMainUrl;

    @Value("${upload.product-detail-url}") // 예: /images/product/detail
    private String productDetailUrl;


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

    /**
     * 상품 등록
     * - 업로드는 물리 경로(productMainDir/detailDir)로 하고
     * - DB에는 공개 URL(/upload/...)을 저장한다.
     * - 예외 발생 시 이번 업로드 파일을 즉시 삭제한다.
     */
    @Transactional
    public int regist(ProductRegistRequest req,
                      MultipartFile mainImage,
                      List<MultipartFile> detailImages) {

        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.categoryId()));
        Artist artist = artistRepository.findById(req.artistId())
                .orElseThrow(() -> new IllegalArgumentException("Artist not found: " + req.artistId()));

        Product product = toEntity(req, category, artist);
        productRepository.saveAndFlush(product); // PK 발급

        List<Path> uploadedPaths = new ArrayList<>();

        try {
            // 메인 이미지
            if (mainImage != null && !mainImage.isEmpty()) {
                String baseDir = productMainDir + "/p" + product.getId(); // 물리 경로
                String fname = UploadManager.storeAndReturnName(mainImage, baseDir);
                uploadedPaths.add(Paths.get(baseDir, fname));

                String url = productMainUrl+"/p" + product.getId() + "/" + fname; // 공개 URL
                ProductImage mainImg = new ProductImage();
                mainImg.setProduct(product);
                mainImg.setRole(ProductImage.Role.MAIN);
                mainImg.setOriginalName(mainImage.getOriginalFilename());
                mainImg.setMimeType(mainImage.getContentType());
                mainImg.setSize(mainImage.getSize());
                mainImg.setUrl(url);
                productImageRepository.saveAndFlush(mainImg);
            }

            // 상세 이미지들
            if (detailImages != null && !detailImages.isEmpty()) {
                String baseDir = productDetailDir + "/p" + product.getId(); // 물리 경로
                List<ProductImage> details = new ArrayList<>();
                for (MultipartFile file : detailImages) {
                    if (file == null || file.isEmpty()) continue;

                    String fname = UploadManager.storeAndReturnName(file, baseDir);
                    uploadedPaths.add(Paths.get(baseDir, fname));

                    String url = productDetailUrl+"/p" + product.getId() + "/" + fname; // 공개 URL
                    ProductImage pi = new ProductImage();
                    pi.setProduct(product);
                    pi.setRole(ProductImage.Role.DETAIL);
                    pi.setOriginalName(file.getOriginalFilename());
                    pi.setMimeType(file.getContentType());
                    pi.setSize(file.getSize());
                    pi.setUrl(url);
                    details.add(pi);
                }
                if (!details.isEmpty()) productImageRepository.saveAllAndFlush(details);
            }

            return product.getId();

        } catch (IOException e) {
            // 디스크 롤백
            for (Path p : uploadedPaths) deleteQuietly(p);
            throw new RuntimeException("File save failed", e);
        }
    }

    /**
     * 상품 상세 조회
     * - DB에 저장된 공개 URL(/upload/...)을 그대로 내려준다.
     */
    @Transactional(readOnly = true)
    @Override
    public ProductDetailResponse getProductDetail(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        List<ProductImage> images = productImageRepository.findByProductId(productId);

        String mainImageUrl = images.stream()
                .filter(img -> img.getRole() == ProductImage.Role.MAIN)
                .findFirst()
                .map(ProductImage::getUrl)
                .orElse(null);

        List<ProductDetailResponse.ImageDto> detailImages = images.stream()
                .filter(img -> img.getRole() == ProductImage.Role.DETAIL)
                .map(img -> new ProductDetailResponse.ImageDto(img.getId(), img.getUrl()))
                .toList();

        return ProductDetailResponse.from(product, mainImageUrl, detailImages);
    }

    /**
     * 상품 수정
     * - 기본 필드 갱신
     * - 메인 이미지: 새 파일이 오면 교체(기존 메인은 DB/파일 삭제), 아니면 유지
     * - 상세 이미지: 기본 동작은 "추가(append)"만 한다 → 기존 상세 이미지는 삭제하지 않음
     *   (전체 교체가 필요하면 별도 플래그/엔드포인트로 분리하는 것을 권장)
     * - 파일 삭제는 트랜잭션 커밋 후에 수행(rollback 시 이번 업로드 파일 삭제)
     */
    @Override
    @Transactional
    public int update(int productId,
                      ProductRegistRequest req,
                      MultipartFile mainImage,
                      List<MultipartFile> detailImages,
                      List<Integer> deleteDetailImageIds) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.categoryId()));
        Artist artist = artistRepository.findById(req.artistId())
                .orElseThrow(() -> new IllegalArgumentException("Artist not found: " + req.artistId()));

        // 1) 기본 필드 갱신
        product.setName(req.productName());
        product.setDescription(req.detail());
        product.setPrice(req.price());
        product.setOpenDate(req.salesOpenAt());
        product.setFanOnly(req.fanLimited());
        product.setStockQuantity(req.initialStock());
        product.setLimitPerUser(req.purchaseLimit());
        product.setCategory(category);
        product.setArtist(artist);
        productRepository.saveAndFlush(product);

        if (detailImages == null) detailImages = List.of();

        // 2) 기존 이미지 조회
        List<ProductImage> oldAll = productImageRepository.findByProductId(productId);
        List<ProductImage> oldMains   = oldAll.stream().filter(pi -> pi.getRole() == ProductImage.Role.MAIN).toList();
        // 상세(oldDetails)는 보존이 기본이므로 조회만 하고 삭제하지 않음

        // 물리 경로(업로드 디렉토리)
        String mainBaseDir   = productMainDir   + "/p" + productId;
        String detailBaseDir = productDetailDir + "/p" + productId;

        // 롤백 시 삭제할 신규 파일, 커밋 후 삭제할 이전 파일(메인만)
        List<Path> uploadedNow = new ArrayList<>();
        List<Path> oldFilesToDelete = new ArrayList<>();

        try {
            // 3) 메인 이미지 교체 (파일이 왔을 때만)
            if (mainImage != null && !mainImage.isEmpty()) {
                // 새 메인 저장
                String fname = UploadManager.storeAndReturnName(mainImage, mainBaseDir);
                uploadedNow.add(Paths.get(mainBaseDir, fname));

                String url = productMainUrl+"/p" + productId + "/" + fname; // 공개 URL
                ProductImage newMain = new ProductImage();
                newMain.setProduct(product);
                newMain.setRole(ProductImage.Role.MAIN);
                newMain.setOriginalName(mainImage.getOriginalFilename());
                newMain.setMimeType(mainImage.getContentType());
                newMain.setSize(mainImage.getSize());
                newMain.setUrl(url);
                productImageRepository.saveAndFlush(newMain);

                // 기존 메인 DB 삭제 + 파일 삭제 예약
                if (!oldMains.isEmpty()) {
                    for (ProductImage pi : oldMains) {
                        Path phys = toPhysicalPath(pi.getUrl());
                        if (phys != null) oldFilesToDelete.add(phys);
                    }
                    productImageRepository.deleteAllInBatch(oldMains);
                }
            }

            // 4) 상세 이미지는 "추가(append)"만 수행 (전면 교체 금지)
            if (!detailImages.isEmpty()) {
                List<ProductImage> toSave = new ArrayList<>();
                for (MultipartFile file : detailImages) {
                    if (file == null || file.isEmpty()) continue;

                    String fname = UploadManager.storeAndReturnName(file, detailBaseDir);
                    uploadedNow.add(Paths.get(detailBaseDir, fname));

                    String url = productDetailUrl+"/p" + productId + "/" + fname; // 공개 URL
                    ProductImage pi = new ProductImage();
                    pi.setProduct(product);
                    pi.setRole(ProductImage.Role.DETAIL);
                    pi.setOriginalName(file.getOriginalFilename());
                    pi.setMimeType(file.getContentType());
                    pi.setSize(file.getSize());
                    pi.setUrl(url);
                    toSave.add(pi);
                }
                if (!toSave.isEmpty()) productImageRepository.saveAllAndFlush(toSave);
            }

            // 5) 상세 이미지 ‘선택 삭제’ 처리
            if (deleteDetailImageIds != null && !deleteDetailImageIds.isEmpty()) {
                // 해당 product의 DETAIL만 대상으로 안전하게 필터링
                List<ProductImage> toDelete = productImageRepository.findAllById(deleteDetailImageIds).stream()
                        .filter(pi -> pi.getProduct().getId() == productId)
                        .filter(pi -> pi.getRole() == ProductImage.Role.DETAIL)
                        .toList();

                if (!toDelete.isEmpty()) {
                    for (ProductImage pi : toDelete) {
                        Path phys = toPhysicalPath(pi.getUrl()); // 공개 URL -> 물리경로로
                        if (phys != null) oldFilesToDelete.add(phys);
                    }
                    productImageRepository.deleteAllInBatch(toDelete);
                }
            }

            // 5) 트랜잭션 후처리: 커밋되면 이전 메인 파일 삭제, 롤백이면 이번 업로드 삭제
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    for (Path p : oldFilesToDelete) deleteQuietly(p);
                    cleanupEmptyParents(oldFilesToDelete);
                }

                @Override
                public void afterCompletion(int status) {
                    if (status != STATUS_COMMITTED) {
                        for (Path p : uploadedNow) deleteQuietly(p);
                        cleanupEmptyParents(uploadedNow);
                    }
                }
            });

            return product.getId();

        } catch (IOException e) {
            // 저장 중 예외 → 이번 업로드 파일만 정리
            for (Path p : uploadedNow) deleteQuietly(p);
            throw new RuntimeException("File save failed", e);
        }
    }

    /**
     * 상품 삭제 (미구현)
     * - 필요 시: 이미지 레코드/파일 삭제 + 상품 상태 변경(soft delete) 등 정책 적용
     */
    @Override
    public void delete(int productId) {
        // TODO: 구현 필요 시 정책에 맞게 작성
    }

    // ===== 유틸 =====

    /** 조용히 파일 삭제 */
    private void deleteQuietly(Path p) {
        try { Files.deleteIfExists(p); }
        catch (IOException ex) { log.warn("파일 삭제 실패: {}", p, ex); }
    }

    /** 비워진 상위 폴더 정리(최소 범위) */
    private void cleanupEmptyParents(List<Path> paths) {
        Set<Path> parents = new HashSet<>();
        for (Path p : paths) {
            if (p != null && p.getParent() != null) parents.add(p.getParent());
        }
        for (Path dir : parents) {
            try {
                if (Files.exists(dir) && Files.isDirectory(dir) && Files.list(dir).findAny().isEmpty()) {
                    Files.delete(dir);
                }
            } catch (IOException ex) {
                log.debug("빈 폴더 정리 실패(무시): {}", dir, ex);
            }
        }
    }

    /**
     * 공개 URL(/upload/...) → 물리 경로(productMainDir/detailDir) 변환
     * - 과거에 절대 물리경로가 저장된 레코드 호환도 포함
     */
    private Path toPhysicalPath(String url) {
        if (url == null || url.isBlank()) return null;

        if (url.startsWith(productMainDir)) {
            String rest = url.substring(productMainDir.length()); // p{productId}/filename
            return Paths.get(productMainDir).resolve(rest.replace("/", FileSystems.getDefault().getSeparator()));
        }
        if (url.startsWith(productDetailUrl)) {
            String rest = url.substring(productDetailUrl.length());
            return Paths.get(productDetailDir).resolve(rest.replace("/", FileSystems.getDefault().getSeparator()));
        }
        return null;
    }

    /** Request -> Entity 변환 */
    private Product toEntity(ProductRegistRequest r, Category c, Artist a) {
        Product p = new Product();
        p.setName(r.productName());
        p.setDescription(r.detail());
        p.setPrice(r.price());
        p.setOpenDate(r.salesOpenAt());
        p.setFanOnly(r.fanLimited());
        p.setStockQuantity(r.initialStock());
        p.setLimitPerUser(r.purchaseLimit());
        p.setStatus(r.productStatus());
        p.setCategory(c);
        p.setArtist(a);
        return p;
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

    @Override
    public List<Product> findByArtistId(int artistId) {
        return productRepository.findByArtistId(artistId);
    }
}
