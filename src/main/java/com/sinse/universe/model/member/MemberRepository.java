package com.sinse.universe.model.member;

import com.sinse.universe.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    // 특정 아티스트의 모든 멤버 조회
    List<Member> findByArtistId(int artistId);
}