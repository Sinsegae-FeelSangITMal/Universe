package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    // 특정 소속사의 모든 아티스트 조회
    // Artist ID로 소속 Member 조회
    List<Artist> findByPartnerId(int partnerId);

}