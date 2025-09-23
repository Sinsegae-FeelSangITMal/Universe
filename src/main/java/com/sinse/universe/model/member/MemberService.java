package com.sinse.universe.model.member;

import com.sinse.universe.domain.Member;

import java.util.List;

public interface MemberService {
    // 멤버 등록
    void regist(Member member);

    // 멤버 수정
    void update(Member member);

    // 멤버 삭제
    void delete(int memberId);

    // 멤버 단건 조회 (수정/삭제 시 사용)
    public Member findById(int memberId) ;

    // 아티스트 ID로 멤버 목록 조회
    public List<Member> findByArtistId(int artistId) ;
}
