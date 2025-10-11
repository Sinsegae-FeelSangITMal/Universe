package com.sinse.universe.model.stream;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Promotion;
import com.sinse.universe.domain.Stream;
import com.sinse.universe.dto.request.StreamRequest;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.enums.StreamStatus;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.promotion.PromotionRepository;
import com.sinse.universe.util.ObjectStorageService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StreamServiceImpl implements StreamService {

    private final StreamRepository streamRepository;
    private final ArtistRepository artistRepository;
    private final PromotionRepository promotionRepository;
    private final ObjectStorageService objectStorageService;

    public StreamServiceImpl(StreamRepository streamRepository,
                             ArtistRepository artistRepository,
                             PromotionRepository promotionRepository,
                             ObjectStorageService objectStorageService) {
        this.streamRepository = streamRepository;
        this.artistRepository = artistRepository;
        this.promotionRepository = promotionRepository;
        this.objectStorageService = objectStorageService;
    }

    @Override
    public List<Stream> selectAll() {
        return streamRepository.findAll();
    }

    @Override
    public Stream select(int streamId) {
        return streamRepository.findById(streamId)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));
    }

    /** 라이브 등록 */
    @Override
    @Transactional
    public Stream regist(StreamRequest request) throws IOException {
        Stream stream = new Stream();
        stream.setTitle(request.getTitle());
        stream.setTime(request.getTime());
        stream.setFanOnly(Boolean.TRUE.equals(request.getFanOnly()));
        stream.setProdLink(Boolean.TRUE.equals(request.getProdLink()));
        stream.setPrYn(Boolean.TRUE.equals(request.getPrYn()));

        // 아티스트 매핑
        if (request.getArtistId() != null) {
            Artist artist = artistRepository.findById(request.getArtistId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));
            stream.setArtist(artist);
        }

        // 프로모션 매핑
        if (request.getPromotionId() != null) {
            Promotion promotion = promotionRepository.findById(request.getPromotionId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PROMOTION_NOT_FOUND));
            stream.setPromotion(promotion);
        }

        // 먼저 저장해서 PK 확보
        Stream saved = streamRepository.saveAndFlush(stream);

        // 썸네일 업로드 (비공개 업로드 → DB에는 프록시 경로 저장)
        MultipartFile thumb = request.getThumb();
        if (thumb != null && !thumb.isEmpty()) {
            String key = objectStorageService.store(thumb, "stream/s" + saved.getId());
            saved.setThumb("/images/" + key);  // 프론트는 /images/** 로 접근
        }

        return streamRepository.save(saved);
    }

    /** 라이브 수정 */
    @Override
    @Transactional
    public Stream update(int id, StreamRequest request) throws IOException {
        Stream existing = streamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));

        // 기본 필드
        existing.setTitle(request.getTitle());
        existing.setTime(request.getTime());
        existing.setFanOnly(Boolean.TRUE.equals(request.getFanOnly()));
        existing.setProdLink(Boolean.TRUE.equals(request.getProdLink()));
        existing.setPrYn(Boolean.TRUE.equals(request.getPrYn()));

        // 아티스트
        if (request.getArtistId() != null) {
            Artist artist = artistRepository.findById(request.getArtistId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));
            existing.setArtist(artist);
        } else {
            existing.setArtist(null);
        }

        // 프로모션
        if (Boolean.TRUE.equals(request.getPrYn()) && request.getPromotionId() != null) {
            Promotion promotion = promotionRepository.findById(request.getPromotionId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PROMOTION_NOT_FOUND));
            existing.setPromotion(promotion);
        } else {
            existing.setPromotion(null);
        }

        // 썸네일 교체/삭제
        MultipartFile newThumb = request.getThumb();
        boolean deleteThumbOnly = Boolean.TRUE.equals(request.getDeleteThumb()); // <-- StreamRequest에 필드 추가 필요

        if (newThumb != null && !newThumb.isEmpty()) {
            // 교체: 기존 삭제 → 새 업로드
            deleteObjectByUrl(existing.getThumb());
            String key = objectStorageService.store(newThumb, "stream/s" + existing.getId());
            existing.setThumb("/images/" + key);
        } else if (deleteThumbOnly) {
            // 삭제만: 기존 삭제 → DB null
            deleteObjectByUrl(existing.getThumb());
            existing.setThumb(null);
        }

        return streamRepository.save(existing);
    }

    /** 라이브 삭제 */
    @Override
    @Transactional
    public void delete(int streamId) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));

        // 1) 현재 DB에 저장된 썸네일만 개별 삭제
        deleteObjectByUrl(stream.getThumb());

        // 2) 혹시 남아있을 수 있는 과거 교체본까지 한 번에 정리
        objectStorageService.deleteFolderPrefix("stream/s" + stream.getId());

        streamRepository.delete(stream);
    }

    @Override
    public Page<Stream> findByArtistId(int artistId, Pageable pageable) {
        return streamRepository.findByArtistId(artistId, pageable);
    }

    @Override
    public List<Stream> findByArtistId(int artistId) {
        return streamRepository.findByArtistId(artistId);
    }

    @Override
    @Transactional
    public Stream updateStatusToLive(int id) {
        Stream stream = streamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));

        stream.setStatus(StreamStatus.LIVE);
        stream.setTime(LocalDateTime.now());
        return streamRepository.save(stream);
    }

    @Override
    @Transactional
    public Stream updateRecord(int id, String record) {
        Stream s = streamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));

        s.setRecord(record);
        s.setStatus(StreamStatus.ENDED);
        s.setEndTime(LocalDateTime.now());
        return streamRepository.save(s);
    }

    /* ===== 유틸 ===== */

    /** DB에 저장된 "/images/{objectKey}"에서 objectKey만 추출 */
    private String extractKey(String url) {
        if (url == null || url.isBlank()) return null;
        final String prefix = "/images/";
        return url.startsWith(prefix) ? url.substring(prefix.length()) : null;
    }

    /** 스토리지에서 객체 삭제 */
    private void deleteObjectByUrl(String url) {
        String key = extractKey(url);
        if (key != null && !key.isBlank()) {
            objectStorageService.deleteObject(key);
        }
    }
}
