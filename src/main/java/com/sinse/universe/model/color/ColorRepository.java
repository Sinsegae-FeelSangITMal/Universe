package com.sinse.universe.model.color;

import com.sinse.universe.domain.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color,Integer> {
    // 특정 아티스트별 단일 색상 조회
    Color findByArtistId(int artistId);
}