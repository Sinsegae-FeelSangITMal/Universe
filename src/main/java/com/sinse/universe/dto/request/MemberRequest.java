package com.sinse.universe.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record MemberRequest(
        String name,
        MultipartFile img,
        int artistId,
        // 이미지 삭제 여부 플래그
        Boolean deleteImg
) {}

