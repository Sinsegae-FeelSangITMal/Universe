package com.sinse.universe.controller;

import com.sinse.universe.util.ObjectStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Duration;

@Slf4j
@Controller
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ObjectStorageService objectStorageService;

    @GetMapping("/**")
    public RedirectView imageProxy(HttpServletRequest request) {
        // 요청 URL에서 "/images/" 이후의 경로(오브젝트 키)를 추출
        // 예: /images/product/detail/some-file.png -> product/detail/some-file.png
        String objectKey = request.getRequestURI().substring("/images/".length());

        // 10분 동안 유효한 Presigned URL 생성
        String presignedUrl = objectStorageService.getPresignedGetUrl(objectKey, Duration.ofMinutes(10));

        log.debug("------ presignedUrl : " + presignedUrl);
        log.debug("------ objectKey  : " + objectKey);
        // 생성된 URL로 리다이렉트
        return new RedirectView(presignedUrl);
    }
}
