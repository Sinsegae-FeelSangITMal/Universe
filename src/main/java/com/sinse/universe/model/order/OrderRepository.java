package com.sinse.universe.model.order;

import com.sinse.universe.domain.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findByUser_IdOrderByDateDesc(int userId);

    // 특정 아티스트의 주문 목록만 가져오기
    @Query("SELECT DISTINCT o       FROM Order o " +
            "JOIN FETCH o.orderProducts op " +
            "JOIN FETCH op.product p " +
            "JOIN FETCH p.artist a " +
            "JOIN FETCH a.partner pt " +
            "WHERE       a.id = :artistId " +
            "ORDER BY   o.no DESC")
    List<Order> findByArtistId(@Param("artistId") int artistId);

    // 특정 소속사의 주문 목록만 가져오기
    @Query("SELECT DISTINCT o       FROM Order o " +
            "JOIN FETCH o.orderProducts op " +
            "JOIN FETCH op.product p " +
            "JOIN FETCH p.artist a " +
            "JOIN FETCH a.partner pt " +
            "WHERE       pt.id = :partnerId " +
            "ORDER BY   o.no DESC")
    List<Order> findByPartnerId(@Param("partnerId") int partnerId);
}