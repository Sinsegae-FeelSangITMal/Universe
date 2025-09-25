package com.sinse.universe.model.member;

import com.sinse.universe.domain.Member;
import com.sinse.universe.dto.request.MemberRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MemberService {

    // 멤버 등록
    public void regist(Member member, MultipartFile img) throws IOException;

    // 멤버 수정
    public void update(Member member, MultipartFile img, boolean deleteImg) throws IOException;

    // 멤버 삭제
    public void delete(int memberId);

    // 멤버 단건 조회 (수정/삭제 시 사용)
    public Member findById(int memberId) ;

    // 아티스트 ID로 멤버 목록 조회
    public List<Member> findByArtistId(int artistId) ;
}
