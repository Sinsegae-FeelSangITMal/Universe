package com.sinse.universe.model.member;

import com.sinse.universe.domain.Member;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
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
        // 소속 아티스트 확인
        if (member.getArtist() == null) {
            throw new CustomException(ErrorCode.MEMBER_ARTIST_REQUIRED);
        }

        // 1차 저장 (PK 생성용)
        memberRepository.saveAndFlush(member);

        if (img != null && !img.isEmpty()) {
            String dir = memberBaseDir + "/a" + member.getArtist().getId() + "/m" + member.getId();
            String filename = UploadManager.storeAndReturnName(img, dir);

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
                .orElseThrow(() -> new CustomException(ErrorCode .MEMBER_NOT_FOUND) );

        // 소속 아티스트 검사
        if (member.getArtist() == null) {
            throw new CustomException(ErrorCode.MEMBER_ARTIST_REQUIRED);
        }

        existing.setName(member.getName());
        existing.setArtist(member.getArtist());

        // 1. 이미지 삭제
        if (deleteImg && existing.getImg() != null) {
            Path oldPath = Paths.get(baseDir).resolve(existing.getImg().replaceFirst("^" + urlPrefix + "/", ""));
            try {
                Files.deleteIfExists(oldPath);
            } catch (IOException e) {
                log.warn("기존 멤버 이미지 삭제 실패: {}", oldPath, e);
            }
            existing.setImg(null);
        }

        // 2. 새 이미지 업로드
        if (img != null && !img.isEmpty()) {
            if (existing.getImg() != null) {
                Path oldPath = Paths.get(baseDir).resolve(existing.getImg().replaceFirst("^" + urlPrefix + "/", ""));
                Files.deleteIfExists(oldPath);
            }

            String dir = memberBaseDir + "/a" + member.getArtist().getId() + "/m" + member.getId();
            String filename = UploadManager.storeAndReturnName(img, dir);

            String url = memberUrl + "/a" + member.getArtist().getId() + "/m" + member.getId() + "/" + filename;
            existing.setImg(url);
        }

        memberRepository.save(existing);
    }

    // 멤버 삭제
    @Override
    @Transactional
    public void delete(int memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 🔹 이미지 파일 정리 (있다면)
        if (member.getImg() != null) {
            Path oldPath = Paths.get(baseDir).resolve(member.getImg().replaceFirst("^" + urlPrefix + "/", ""));
            try {
                Files.deleteIfExists(oldPath);
            } catch (IOException e) {
                log.warn("멤버 이미지 삭제 실패: {}", oldPath, e);
            }
        }

        memberRepository.delete(member);
    }

    // 멤버 단건 조회
    @Override
    public Member findById(int memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // 아티스트 ID로 멤버 목록 조회
    @Override
    public List<Member> findByArtistId(int artistId) {
        return memberRepository.findByArtistId(artistId);
    }
}
