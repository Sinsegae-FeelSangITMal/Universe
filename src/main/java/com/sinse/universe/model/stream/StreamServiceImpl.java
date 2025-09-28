package com.sinse.universe.model.stream;

import com.sinse.universe.domain.*;
import com.sinse.universe.dto.request.StreamRequest;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.artist.ArtistRepository;
import com.sinse.universe.model.product.ProductRepository;
import com.sinse.universe.model.promotion.PromotionRepository;
import com.sinse.universe.model.promotion.PromotionService;
import com.sinse.universe.model.streamProduct.StreamProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreamServiceImpl implements StreamService {

    private final StreamRepository streamRepository;
    private final ArtistRepository artistRepository;
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    private final StreamProductRepository streamProductRepository;

    public StreamServiceImpl(StreamRepository streamRepository, ArtistRepository artistRepository, PromotionRepository promotionRepository, ProductRepository productRepository, StreamProductRepository streamProductRepository) {
        this.streamRepository = streamRepository;
        this.artistRepository = artistRepository;
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
        this.streamProductRepository = streamProductRepository;
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

//    @Override
//    @Transactional
//    public int regist(StreamRequest request) {
//        Stream stream = new Stream();
//        stream.setTitle(request.getTitle());
//        stream.setTime(request.getTime());
//        stream.setFanOnly(request.isFanOnly());
//        stream.setProdLink(request.isProdLink());
//        stream.setPrYn(request.isPrYn());
//
//        // 아티스트 조회
//        Artist artist = artistRepository.findById(request.getArtist().getId())
//                .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));
//        stream.setArtist(artist);
//
//        // 프로모션 처리
//        if (request.isPrYn()) {
//            if (request.getPromotion() == null) {
//                throw new CustomException(ErrorCode.PROMOTION_REQUIRED);
//            }
//            Promotion promotion = promotionRepository.findById(request.getPromotion().getId())
//                    .orElseThrow(() -> new CustomException(ErrorCode.PROMOTION_NOT_FOUND));
//            stream.setPromotion(promotion);
//        }
//
//        streamRepository.save(stream);
//
//        // ✅ 상품 연계 처리
//        if (request.isProdLink()) {
//            if (request.getProductIds() == null || request.getProductIds().isEmpty()) {
//                throw new CustomException(ErrorCode.PRODUCT_REQUIRED);
//            }
//
//            for (Integer productId : request.getProductIds()) {
//                Product product = productRepository.findById(productId)
//                        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
//
//                StreamProduct sp = new StreamProduct();
//                sp.setStream(stream);
//                sp.setProduct(product);
//                streamProductRepository.save(sp);
//            }
//        }
//
//        return stream.getId();
//    }
//
//    @Override
//    @Transactional
//    public void update(int streamId, StreamRequest request) {
//        Stream stream = streamRepository.findById(streamId)
//                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));
//
//        // ✅ 기본 정보 수정
//        stream.setTitle(request.getTitle());
//        stream.setTime(request.getTime());
//        stream.setFanOnly(request.isFanOnly());
//        stream.setProdLink(request.isProdLink());
//        stream.setPrYn(request.isPrYn());
//
//        // ✅ 아티스트 수정
//        Artist artist = artistRepository.findById(request.getArtist().getId())
//                .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));
//        stream.setArtist(artist);
//
//        // ✅ 프로모션 수정
//        if (request.isPrYn()) {
//            if (request.getPromotion() == null) {
//                throw new CustomException(ErrorCode.PROMOTION_REQUIRED);
//            }
//            Promotion promotion = promotionRepository.findById(request.getPromotion().getId())
//                    .orElseThrow(() -> new CustomException(ErrorCode.PROMOTION_NOT_FOUND));
//            stream.setPromotion(promotion);
//        } else {
//            // 프로모션 해제
//            stream.setPromotion(null);
//        }
//
//        // ✅ 상품 수정
//        if (request.isProdLink()) {
//            if (request.getProductIds() == null || request.getProductIds().isEmpty()) {
//                throw new CustomException(ErrorCode.PRODUCT_REQUIRED);
//            }
//
//            // 기존 stream_product 삭제
//            streamProductRepository.deleteByStreamId(streamId);
//
//            // 새 product 등록
//            for (Integer productId : request.getProductIds()) {
//                Product product = productRepository.findById(productId)
//                        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
//
//                StreamProduct sp = new StreamProduct();
//                sp.setStream(stream);
//                sp.setProduct(product);
//                streamProductRepository.save(sp);
//            }
//        } else {
//            // 상품 연계 해제
//            streamProductRepository.deleteByStreamId(streamId);
//        }
//
//        streamRepository.save(stream);
//    }

    @Override
    public int regist(StreamRequest request) {
        Artist artist = artistRepository.findById(request.getArtist().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));

        Stream stream = new Stream();
        stream.setTitle(request.getTitle());
        stream.setTime(request.getTime());
        stream.setFanOnly(request.isFanOnly());
        stream.setProdLink(request.isProdLink());
        stream.setPrYn(request.isPrYn());
        stream.setArtist(artist);

        if (request.isPrYn() && request.getPromotion() != null) {
            Promotion promotion = promotionRepository.findById(request.getPromotion().getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PROMOTION_NOT_FOUND));
            stream.setPromotion(promotion);
        }

        streamRepository.save(stream);
        return stream.getId();
    }

    @Override
    public void update(int id, StreamRequest request) {
        Stream existing = streamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));

        existing.setTitle(request.getTitle());
        existing.setTime(request.getTime());
        existing.setFanOnly(request.isFanOnly());
        existing.setProdLink(request.isProdLink());
        existing.setPrYn(request.isPrYn());

        if (request.isPrYn() && request.getPromotion() != null) {
            Promotion promotion = promotionRepository.findById(request.getPromotion().getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PROMOTION_NOT_FOUND));
            existing.setPromotion(promotion);
        } else {
            existing.setPromotion(null);
        }

        streamRepository.save(existing);
    }


    @Override
    public void delete(int streamId) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));

        streamRepository.delete(stream);
    }

    // 아티스트 ID로 조회
    @Override
    public List<Stream> findByArtistId(int artistId) {
        return streamRepository.findByArtistId(artistId);
    }
}
