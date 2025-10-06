package com.sinse.universe.controller;

import com.sinse.universe.domain.Stream;
import com.sinse.universe.dto.request.StreamRequest;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.StreamResponse;
import com.sinse.universe.enums.StreamStatus;
import com.sinse.universe.model.stream.StreamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ent/streams")
public class StreamController {

    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    // 전체 라이브 목록
    @GetMapping
    public ResponseEntity<ApiResponse<List<StreamResponse>>> getStreams() {
        List<StreamResponse> list = streamService.selectAll()
                .stream()
                .map(StreamResponse::from)
                .toList();
        return ApiResponse.success("라이브 목록 조회 성공", list);
    }

    // 단일 라이브 상세
    @GetMapping("/{streamId}")
    public ResponseEntity<ApiResponse<StreamResponse>> getStream(@PathVariable int streamId) {
        Stream stream = streamService.select(streamId);
        return ApiResponse.success("라이브 조회 성공", StreamResponse.from(stream));
    }

    // 라이브 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StreamResponse>> registStream(
            @ModelAttribute StreamRequest request) throws IOException {
        Stream stream = streamService.regist(request);
        return ApiResponse.success("라이브 등록 성공", StreamResponse.from(stream));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StreamResponse>> updateStream(
            @PathVariable int id,
            @ModelAttribute StreamRequest request) throws IOException {
        Stream stream = streamService.update(id, request);
        return ApiResponse.success("라이브 수정 성공", StreamResponse.from(stream));
    }

    // 라이브 삭제
    @DeleteMapping("/{streamId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteStream(@PathVariable int streamId) {
        streamService.delete(streamId);
        return ApiResponse.success("라이브 삭제 성공", Map.of("id", streamId));
    }

    // 특정 아티스트의 라이브 목록
//    @GetMapping(params = "artistId")
//    public ResponseEntity<ApiResponse<List<StreamResponse>>> getStreamsByArtist(
//            @RequestParam Integer artistId) {
//
//        List<StreamResponse> list = streamService.findByArtistId(artistId)
//                .stream()
//                .map(StreamResponse::from)
//                .toList();
//
//        return ApiResponse.success("특정 아티스트 라이브 목록 조회 성공", list);
//    }
    @GetMapping("/artists/{artistId}/streams")
    public Page<StreamResponse> getStreamsByArtist(
            @PathVariable int artistId,
            @PageableDefault(size = 10, sort = "time", direction = Sort.Direction.DESC) Pageable pageable) {
        return streamService.findByArtistId(artistId, pageable)
                .map(StreamResponse::from);
    }


    // 종료된 스트림만 가져오기
    @GetMapping("/artists/{artistId}/streams/ended")
    public List<StreamResponse> getEnded(
            @PathVariable int artistId
    ) {
        return streamService.findByArtistId(artistId)
                .stream()
                .filter(stream -> stream.getStatus() == StreamStatus.ENDED)
                .map(StreamResponse::from)
                .toList();
    }

}

