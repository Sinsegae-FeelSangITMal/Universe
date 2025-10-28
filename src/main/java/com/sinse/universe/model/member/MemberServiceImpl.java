package com.sinse.universe.model.member;

import com.sinse.universe.domain.Member;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.util.ObjectStorageService; // ✅
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ArtistRepository artistRepository;
    private final ObjectStorageService objectStorageService; // ✅

    public MemberServiceImpl(MemberRepository memberRepository,
                             ArtistRepository artistRepository,
                             ObjectStorageService objectStorageService) {
        this.memberRepository = memberRepository;
        this.artistRepository = artistRepository;
        this.objectStorageService = objectStorageService;
    }

    @Override
    @Transactional
    public void regist(Member member, MultipartFile img) throws IOException {
        if (member.getArtist() == null) {
            throw new CustomException(ErrorCode.MEMBER_ARTIST_REQUIRED);
        }

        memberRepository.saveAndFlush(member); // PK

        if (img != null && !img.isEmpty()) {
            // objectKey: member/a{artistId}/m{memberId}/{uuid}.ext
            String key = objectStorageService.store(
                    img, "member/a" + member.getArtist().getId() + "/m" + member.getId()
            );
            member.setImg("/images/" + key); // ✅ DB에는 /images/{key}
        }

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void update(Member member, MultipartFile img, boolean deleteImg) throws IOException {
        Member existing = memberRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getArtist() == null) {
            throw new CustomException(ErrorCode.MEMBER_ARTIST_REQUIRED);
        }

        existing.setName(member.getName());
        existing.setArtist(member.getArtist());

        if (deleteImg && existing.getImg() != null) {
            deleteObjectByUrl(existing.getImg());
            existing.setImg(null);
        }

        if (img != null && !img.isEmpty()) {
            if (existing.getImg() != null) deleteObjectByUrl(existing.getImg());
            String key = objectStorageService.store(
                    img, "member/a" + member.getArtist().getId() + "/m" + member.getId()
            );
            existing.setImg("/images/" + key); // ✅
        }

        memberRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(int memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getImg() != null) deleteObjectByUrl(member.getImg());

        memberRepository.delete(member);
    }

    @Override
    public Member findById(int memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public List<Member> findByArtistId(int artistId) {
        return memberRepository.findByArtistId(artistId);
    }

    /** /images/{objectKey} → objectKey */
    private String extractKey(String url) {
        if (url == null) return null;
        final String p = "/images/";
        return url.startsWith(p) ? url.substring(p.length()) : null;
    }

    private void deleteObjectByUrl(String url) {
        String key = extractKey(url);
        if (key != null && !key.isBlank()) {
            objectStorageService.deleteObject(key);
        }
    }
}
