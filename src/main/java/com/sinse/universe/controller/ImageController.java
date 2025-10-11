package com.sinse.universe.controller;

import com.sinse.universe.util.ObjectStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ImageController {

    private final ObjectStorageService objectStorageService;

    // 프론트가 /images/** 또는 /api/images/** 어떤 걸로 요청하건 다 받습니다.
    @GetMapping({"/images/**", "/api/images/**"})
    public RedirectView imageProxy(HttpServletRequest request) {
        String requestUri = request.getRequestURI();                  // 예) /api/images/artist/logo/a12/xxx.jpg
        String ctx = request.getContextPath();                        // 보통 "" 또는 "/앱컨텍스트"
        if (ctx != null && !ctx.isBlank() && requestUri.startsWith(ctx)) {
            requestUri = requestUri.substring(ctx.length());          // 컨텍스트 제거
        }

        // 허용 prefix들
        String[] prefixes = { "/images/", "/api/images/" };

        String objectKey = null;
        for (String p : prefixes) {
            if (requestUri.startsWith(p)) {
                objectKey = requestUri.substring(p.length());         // 예) artist/logo/a12/xxx.jpg
                break;
            }
        }

        if (objectKey == null || objectKey.isBlank()) {
            log.warn("이미지 요청에서 objectKey 추출 실패: requestUri={}", request.getRequestURI());
            // 안전하게 루트로 돌려보내거나 404 처리하는 대신 홈으로 보냄
            return new RedirectView("/");
        }

        // 혹시 인코딩된 경로가 왔다면 복원
        objectKey = URLDecoder.decode(objectKey, StandardCharsets.UTF_8);
        // 방어적 정리: 선행 슬래시 제거
        while (objectKey.startsWith("/")) objectKey = objectKey.substring(1);

        String presignedUrl = objectStorageService.getPresignedGetUrl(objectKey, Duration.ofMinutes(10));

        log.debug("[ImageProxy] key={}, presigned={}", objectKey, presignedUrl);

        return new RedirectView(presignedUrl);
    }
}
