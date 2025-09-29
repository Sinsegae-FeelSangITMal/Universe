package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Member;
import com.sinse.universe.dto.response.PartnerArtistResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    // 특정 소속사의 모든 아티스트 조회 - 응답 객체로 반환
    public List<Artist> findByPartner_Id (int partnerId);

    // 특정 소속사의 모든 아티스트 조회 - 객체로 반환
    // Artist ID로 소속 Member 조회
    List<Artist> findByPartnerId(int partnerId);
    boolean existsByName(String name);
}
