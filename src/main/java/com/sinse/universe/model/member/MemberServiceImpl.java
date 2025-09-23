package com.sinse.universe.model.member;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Member;
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
    public void update(Member member) {
        // memberRepository.save(member);

        // 기존 멤버 조회
        Member existing = memberRepository.findById(member.getId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 변경 가능한 필드만 업데이트
        existing.setName(member.getName());
        existing.setImg(member.getImg());

        // ✅ Artist는 기존 값 유지, 필요할 때만 변경
        if (member.getArtist() != null && member.getArtist().getId() > 0) {
            Artist artist = artistRepository.findById(member.getArtist().getId())
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
