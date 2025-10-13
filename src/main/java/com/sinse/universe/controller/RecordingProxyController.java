package com.sinse.universe.controller;

import com.sinse.universe.util.ObjectStorageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/recording")
public class RecordingProxyController {

    private final ObjectStorageService objectStorageService;

    public RecordingProxyController(ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }

    // 예: /recording/s68/rec_abc.webm  -> 버킷의 recording/s68/rec_abc.webm
    @GetMapping("/{folder}/{streamFolder}/{filename}")
    public ResponseEntity<InputStreamResource> getRecording(
            @PathVariable String folder,          // 보통 "s68" 형태로 들어오게 라우팅 설계했으면 변수명 맞춰 조정
            @PathVariable String streamFolder,    // 위 설계가 다르면 PathVariable 개수를 바꿔도 됨
            @PathVariable String filename
    ) {
        // 실제 오브젝트 키 조립 (이미지 쪽 컨벤션과 동일하게)
        // 아래 예시는 /recording/s{ID}/{filename} 구조를 recording/s{ID}/{filename} 키로 매핑
        String objectKey = String.format("recording/%s/%s", folder, filename);
        if (streamFolder != null && !streamFolder.isBlank()) {
            // 라우팅 변수 사용 방식에 따라 조정
            objectKey = String.format("recording/%s/%s/%s", folder, streamFolder, filename);
        }

        InputStream in = objectStorageService.getObjectStream(objectKey);
        // 가능하면 Content-Type도 저장 시 함께 메타데이터로 기록해두고 꺼내오자
        String contentType = "video/webm";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(in));
    }

    // ✅ /recording/s72/xxx.webm
    @GetMapping("/{folder}/{filename:.+}")
    public ResponseEntity<InputStreamResource> getRecording2(
            @PathVariable String folder,
            @PathVariable String filename
    ) {
        String key = String.format("recording/%s/%s", folder, filename);
        InputStream in = objectStorageService.getObjectStream(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("video/webm"))
                .body(new InputStreamResource(in));
    }

    // ✅ 혹시 /recording/recording/s72/xxx.webm 형태로도 들어오면 안전하게 처리
    @GetMapping("/{folder}/{streamFolder}/{filename:.+}")
    public ResponseEntity<InputStreamResource> getRecording3(
            @PathVariable String folder,
            @PathVariable String streamFolder,
            @PathVariable String filename
    ) {
        String key = String.format("recording/%s/%s/%s", folder, streamFolder, filename);
        InputStream in = objectStorageService.getObjectStream(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("video/webm"))
                .body(new InputStreamResource(in));
    }
}
