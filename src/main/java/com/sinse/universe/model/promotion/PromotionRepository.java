package com.sinse.universe.model.promotion;

import com.sinse.universe.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    List<Promotion> findByArtist_Id(int artistId);
}
