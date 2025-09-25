package com.sinse.universe.model.member;

import com.sinse.universe.domain.Member;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.util.UploadManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ArtistRepository artistRepository;

    @Value("${upload.base-dir}")
    private String baseDir;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @Value("${upload.member-dir}")
    private String memberBaseDir;

    @Value("${upload.member-url}")
    private String memberUrl;

    public MemberServiceImpl(MemberRepository memberRepository, ArtistRepository artistRepository) {
        this.memberRepository = memberRepository;
        this.artistRepository = artistRepository;
    }

    // 멤버 등록
    @Override
    @Transactional
    public void regist(Member member, MultipartFile img) throws IOException {
        // 먼저 member 저장 (PK 생성용)
        memberRepository.saveAndFlush(member);

        if (img != null && !img.isEmpty()) {
            // /upload/member/a{artistId}/m{memberId}
            String dir = memberBaseDir + "/a" + member.getArtist().getId() + "/m" + member.getId();
            String filename = UploadManager.storeAndReturnName(img, dir);

            // DB에는 웹 접근 가능한 URL 저장
            String url = memberUrl + "/a" + member.getArtist().getId() + "/m" + member.getId() + "/" + filename;
            member.setImg(url);
        }

        memberRepository.save(member);
    }

    // 멤버 수정
    @Override
    @Transactional
    public void update(Member member, MultipartFile img, boolean deleteImg) throws IOException {
        Member existing = memberRepository.findById(member.getId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 기본 정보 수정
        existing.setName(member.getName());
        existing.setArtist(member.getArtist());

        // 1. 이미지 삭제 요청이 있을 경우
        // 1. 이미지 삭제 요청이 있을 경우
        if (deleteImg && existing.getImg() != null) {
            Path oldPath = Paths.get(baseDir).resolve(existing.getImg().replaceFirst("^" + urlPrefix + "/", ""));

            try {
                Files.deleteIfExists(oldPath);
            } catch (IOException e) {
                log.warn("기존 멤버 이미지 삭제 실패: {}", oldPath, e);
            }
            existing.setImg(null); // DB에서도 제거
        }

        // 2. 새 이미지가 업로드된 경우
        if (img != null && !img.isEmpty()) {
            // 기존 파일이 있으면 정리 (중복 방지)
            if (existing.getImg() != null) {
                Path oldPath = Paths.get(baseDir).resolve(existing.getImg().replaceFirst("^" + urlPrefix + "/", ""));

                Files.deleteIfExists(oldPath);
            }

            String dir = memberBaseDir + "/a" + member.getArtist().getId() + "/m" + member.getId();
            String filename = UploadManager.storeAndReturnName(img, dir);

            // DB에는 URL 경로 저장
            String url = memberUrl + "/a" + member.getArtist().getId() + "/m" + member.getId() + "/" + filename;
            member.setImg(url);

        }

        // 3. deleteImg=false AND img=null → 아무 일도 하지 않음
        // 기존 이미지는 그대로 유지

        memberRepository.save(existing);
    }

    // 멤버 삭제
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
