package com.sinse.universe.controller;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Member;
import com.sinse.universe.dto.request.MemberRequest;
import com.sinse.universe.dto.response.MemberResponse;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.member.MemberRepository;
import com.sinse.universe.model.member.MemberService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
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
    @GetMapping(value = "/ent/members", params = "artistId")
    public List<MemberResponse> getMembersByArtist(@RequestParam int artistId) {
        return memberService.findByArtistId(artistId)
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    // 멤버 상세 조회
    @GetMapping("/ent/members/{memberId}")
    public MemberResponse getMember(@PathVariable int memberId) {
        Member member = memberService.findById(memberId);
        return MemberResponse.from(member);
    }

    // 멤버 등록
    @PostMapping(value = "/ent/members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addMember(@ModelAttribute MemberRequest request) throws IOException {

        Artist artist = artistRepository.findById(request.artistId())
                .orElseThrow(() -> new RuntimeException("아티스트를 찾을 수 없습니다."));

        Member member = new Member();
        member.setName(request.name());
        member.setArtist(artist);

        // 서비스 호출 시 DTO 안의 img를 넘김
        memberService.regist(member, request.img());

        return ResponseEntity.ok(Map.of(
                "result", "멤버 등록 성공",
                "id", member.getId()
        ));
    }

    // 멤버 수정
    @PutMapping(value = "/ent/members/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMember(
            @PathVariable int memberId,
            @ModelAttribute MemberRequest request) throws IOException {

        // 기존 데이터 조회
        Member member = memberService.findById(memberId);

        // 기본 정보 업데이트
        member.setName(request.name());
        if (request.artistId() > 0) {
            Artist artist = artistRepository.findById(request.artistId())
                    .orElseThrow(() -> new RuntimeException("Artist not found"));
            member.setArtist(artist);
        }

        // 서비스 호출 시 img와 삭제 여부도 같이 넘김
        memberService.update(member, request.img(), Boolean.TRUE.equals(request.deleteImg())
        );

        return ResponseEntity.ok(Map.of("result", "멤버 수정 성공"));
    }

    // 멤버 삭제
    @DeleteMapping("/ent/members/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable int memberId) {
        memberService.delete(memberId);
        return ResponseEntity.ok(Map.of("result", "멤버 삭제 성공"));
    }
}