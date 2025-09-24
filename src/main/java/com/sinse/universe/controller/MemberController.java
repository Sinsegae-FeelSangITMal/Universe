package com.sinse.universe.controller;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Member;
import com.sinse.universe.dto.request.MemberRequest;
import com.sinse.universe.dto.response.MemberResponse;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.member.MemberRepository;
import com.sinse.universe.model.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ArtistRepository artistRepository;

    public MemberController(MemberService memberService, MemberRepository memberRepository, ArtistRepository artistRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.artistRepository = artistRepository;
    }

    // 특정 아티스트의 모든 멤버 조회 (쿼리 파라미터 방식)
    @GetMapping(value = "/members", params = "artistId")
    public List<MemberResponse> getMembersByArtist(@RequestParam int artistId) {
        return memberService.findByArtistId(artistId)
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    // 멤버 상세 조회
    @GetMapping("/members/{memberId}")
    public Member getMember(@PathVariable int memberId) {
        return memberService.findById(memberId);
    }

    // 멤버 등록
    @PostMapping("/members")
    public ResponseEntity<?> addMember(@RequestBody MemberRequest request) {
        Artist artist = artistRepository.findById(request.artistId())
                .orElseThrow(() -> new RuntimeException("아티스트를 찾을 수 없습니다."));

        Member member = new Member();
        member.setName(request.name());
        member.setImg(request.img());
        member.setArtist(artist); // ✅ 외래키 세팅

        memberService.regist(member);
        return ResponseEntity.ok(Map.of("result", "멤버 등록 성공"));
    }

    @PutMapping("/members/{memberId}")
    public ResponseEntity<?> updateMember(
            @PathVariable int memberId,
            @RequestBody MemberRequest request) {

        memberService.update(memberId, request);
        return ResponseEntity.ok(Map.of("result", "멤버 수정 성공"));
    }

    // 멤버 삭제
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable int memberId) {
        memberService.delete(memberId);
        return ResponseEntity.ok(Map.of("result", "멤버 삭제 성공"));
    }
}