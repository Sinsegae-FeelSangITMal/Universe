package com.sinse.universe.model.member;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Member;
import com.sinse.universe.dto.request.MemberRequest;
import com.sinse.universe.model.artist.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ArtistRepository artistRepository;

    public MemberServiceImpl(MemberRepository memberRepository, ArtistRepository artistRepository) {
        this.memberRepository = memberRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public void regist(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void update(int memberId, MemberRequest member) {

        // 기존 멤버 조회
        Member existing = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 변경 가능한 필드 업데이트
        existing.setName(member.name());
        existing.setImg(member.img());

        // ✅ artistId가 0보다 큰 경우만 변경
        if (member.artistId() > 0) {
            Artist artist = artistRepository.findById(member.artistId())
                    .orElseThrow(() -> new RuntimeException("Artist not found"));
            existing.setArtist(artist);
        }

        memberRepository.save(existing);
    }

    @Override
    public void delete(int memberId) {
        memberRepository.deleteById(memberId);
    }

    // 멤버 단건 조회 (수정/삭제 시 사용)
    @Override
    public Member findById(int memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버를 찾을 수 없습니다: " + memberId));
    }

    // 아티스트 ID로 멤버 목록 조회
    @Override
    public List<Member> findByArtistId(int artistId) {
        return memberRepository.findByArtistId(artistId);
    }
}
