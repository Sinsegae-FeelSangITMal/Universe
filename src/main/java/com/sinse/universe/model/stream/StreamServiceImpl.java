package com.sinse.universe.model.stream;

import com.sinse.universe.domain.*;
import com.sinse.universe.dto.request.StreamRequest;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.promotion.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class StreamServiceImpl implements StreamService {

    private final StreamRepository streamRepository;
    private final ArtistRepository artistRepository;
    private final PromotionRepository promotionRepository;

    public StreamServiceImpl(StreamRepository streamRepository,
                             ArtistRepository artistRepository,
                             PromotionRepository promotionRepository) {
        this.streamRepository = streamRepository;
        this.artistRepository = artistRepository;
        this.promotionRepository = promotionRepository;
    }

    @Value("${upload.stream-dir}")
    private String streamDir;

    @Value("${upload.stream-url}")
    private String streamUrl;

    @Override
    public List<Stream> selectAll() {
        return streamRepository.findAll();
    }

    @Override
    public Stream select(int streamId) {
        return streamRepository.findById(streamId)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));
    }

    // 라이브 등록
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

        // Stream 새로운 라이브 먼저 저장해서 ID 확보
        Stream saved = streamRepository.save(stream);

        // 썸네일 저장
        if (request.getThumb() != null && !request.getThumb().isEmpty()) {
            String savedPath = saveFile(request.getThumb(), saved.getId());
            saved.setThumb(savedPath);
        }

        return streamRepository.save(saved);
    }

    // 라이브 수정
    @Override
    @Transactional
    public Stream update(int id, StreamRequest request) throws IOException {
        Stream existing = streamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));

        // 기본 필드 업데이트
        existing.setTitle(request.getTitle());
        existing.setTime(request.getTime());
        existing.setFanOnly(Boolean.TRUE.equals(request.getFanOnly()));
        existing.setProdLink(Boolean.TRUE.equals(request.getProdLink()));
        existing.setPrYn(Boolean.TRUE.equals(request.getPrYn()));

        // 아티스트 변경
        if (request.getArtistId() != null) {
            Artist artist = artistRepository.findById(request.getArtistId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));
            existing.setArtist(artist);
        } else {
            existing.setArtist(null);
        }

        // 프로모션 변경
        if (Boolean.TRUE.equals(request.getPrYn()) && request.getPromotionId() != null) {
            Promotion promotion = promotionRepository.findById(request.getPromotionId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PROMOTION_NOT_FOUND));
            existing.setPromotion(promotion);
        } else {
            existing.setPromotion(null); // SR_PR_YN=false → PM_ID=null
        }

        // 썸네일 처리
        MultipartFile newThumb = request.getThumb();

        if (newThumb != null && !newThumb.isEmpty()) {
            // 기존 썸네일 삭제
            if (existing.getThumb() != null) {
                String fileName = Paths.get(existing.getThumb()).getFileName().toString();
                Path oldFile = Paths.get(streamDir, "s" + existing.getId(), fileName);
                Files.deleteIfExists(oldFile);
            }

            // 새 파일 저장
            String savedPath = saveFile(newThumb, existing.getId());
            existing.setThumb(savedPath);
        }

        return existing; // @Transactional → flush 시 DB 반영
    }

    // 라이브 삭제
    @Override
    public void delete(int streamId) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));
        streamRepository.delete(stream);
    }

    // 특정 아티스트의 라이브 조회
    @Override
    public List<Stream> findByArtistId(int artistId) {
        return streamRepository.findByArtistId(artistId);
    }

    // 파일 저장 메서드
    private String saveFile(MultipartFile file, Integer streamId) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path dirPath = Paths.get(streamDir, "s" + streamId);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path filePath = dirPath.resolve(filename);
        file.transferTo(filePath.toFile());

        // 현재
        return streamUrl + "/s" + streamId + "/" + filename;
    }

}
