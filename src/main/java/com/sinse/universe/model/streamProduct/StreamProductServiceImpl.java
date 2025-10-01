package com.sinse.universe.model.streamProduct;

import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.Stream;
import com.sinse.universe.domain.StreamProduct;
import com.sinse.universe.dto.request.StreamProductRequest;
import com.sinse.universe.dto.response.StreamProductResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.product.ProductRepository;
import com.sinse.universe.model.stream.StreamRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreamProductServiceImpl implements StreamProductService {

    private final StreamProductRepository streamProductRepository;
    private final StreamRepository streamRepository;
    private final ProductRepository productRepository;

    public StreamProductServiceImpl(StreamProductRepository streamProductRepository,
                                    StreamRepository streamRepository,
                                    ProductRepository productRepository) {
        this.streamProductRepository = streamProductRepository;
        this.streamRepository = streamRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<StreamProductResponse> selectAll() {
        return streamProductRepository.findAll().stream()
                .map(StreamProductResponse::from)
                .toList();
    }

    @Override
    public StreamProductResponse select(int id) {
        StreamProduct sp = streamProductRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_PRODUCT_NOT_FOUND));
        return StreamProductResponse.from(sp);
    }

    @Override
    public List<StreamProductResponse> findByStreamId(int streamId) {
        return streamProductRepository.findByStreamId(streamId).stream()
                .map(StreamProductResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void regist(StreamProductRequest request) {
        Stream stream = streamRepository.findById(request.getStreamId())
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 중복 방지
        if (!streamProductRepository.existsByStreamAndProduct(stream, product)) {
            StreamProduct sp = new StreamProduct();
            sp.setStream(stream);
            sp.setProduct(product);
            streamProductRepository.save(sp);
        }
    }

    @Override
    @Transactional
    public int update(StreamProductRequest request) {
        StreamProduct existing = streamProductRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_PRODUCT_NOT_FOUND));

        Stream stream = streamRepository.findById(request.getStreamId())
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        existing.setStream(stream);
        existing.setProduct(product);

        return streamProductRepository.save(existing).getId();
    }

    @Override
    public void delete(int id) {
        StreamProduct existing = streamProductRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_PRODUCT_NOT_FOUND));
        streamProductRepository.delete(existing);
    }

    @Override
    @Transactional
    public void deleteByStreamId(int streamId) {
        streamProductRepository.deleteByStreamId(streamId);
    }

    @Override
    @Transactional
    public void addProducts(int streamId, List<Integer> productIds) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new CustomException(ErrorCode.STREAM_NOT_FOUND));

        for (Integer productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

            if (!streamProductRepository.existsByStreamAndProduct(stream, product)) {
                StreamProduct sp = new StreamProduct();
                sp.setStream(stream);
                sp.setProduct(product);
                streamProductRepository.save(sp);
            }
        }
    }

    @Override
    public void removeProductsByStream(int streamId) {
        streamProductRepository.deleteByStreamId(streamId);
    }
}
