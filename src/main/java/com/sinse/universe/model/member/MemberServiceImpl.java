package com.sinse.universe.model.member;

import com.sinse.universe.domain.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void regist(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void update(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void delete(int memberId) {
        memberRepository.deleteById(memberId);
    }

    @Override
    public Member select(int memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));
    }

    // 아티스트 ID로 멤버 목록 조회
    public List<Member> findByArtistId(int artistId) {
        return memberRepository.findByArtistId(artistId);
    }

    // 멤버 단건 조회 (수정/삭제 시 사용)
    public Member findById(int memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버를 찾을 수 없습니다: " + memberId));
    }
}
