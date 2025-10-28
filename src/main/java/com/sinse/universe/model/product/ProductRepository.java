package com.sinse.universe.model.product;

import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("""
                select p.id
                from Product p
                where (:artistId is null or p.artist.id = :artistId)
                order by p.id desc
            """)
    Page<Integer> pageIdsByArtist(@Param("artistId") Integer artistId, Pageable pageable);

    @Query("""
                select p from Product p
                join fetch p.category
                join fetch p.artist
                where p.id in :ids
                order by p.id desc
            """)
    List<Product> findWithRefsByIds(@Param("ids") List<Integer> ids);

    // 아티스트의 상품 조회
    List<Product> findByArtistId(int artistId);

    // 상품 최신 목록을 멤버십 제외하고 조회
    @Query("""
            SELECT DISTINCT p
            FROM        Product p
            JOIN         p.category c
            JOIN         p.productImageList pi
            WHERE      p.status = 'ACTIVE'
                AND      pi.role = 'main'
                AND      c.name <> 'Membership'
            ORDER BY  p.registDate DESC, p.id DESC
            """)
    Page<Product> findLatestActiveWithMainExcludingMembership(
            Pageable pageable
    );

    // 그대로 사용
    @Query("""
              select p
              from Product p
              where p.artist.id = :artistId
                and (:categoryId is null or p.category.id = :categoryId)
            """)
    Page<Product> findByArtistAndOptionalCategory(
            @Param("artistId") int artistId,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);

}