package com.sinse.universe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

public record MemberRequest(

        @NotBlank(message = "멤버 이름은 필수입니다.")
        String name,

        // 이미지 업로드는 선택사항 → 검증 불필요
        MultipartFile img,

        @NotNull(message = "아티스트 ID는 필수입니다.")
        Integer artistId,

        // 이미지 삭제 여부 플래그 (null 허용, 컨트롤러/서비스에서 기본값 false로 처리)
        Boolean deleteImg
) {}
