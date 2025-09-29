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

    @Query("""
        select distinct p
        from Product p
        join p.productImageList pi
        where p.status = 'ACTIVE'
          and pi.role = "main"
        order by p.registDate desc, p.id desc
        """)
    Page<Product> findLatestActiveWithMain(
            Pageable pageable
    );

}