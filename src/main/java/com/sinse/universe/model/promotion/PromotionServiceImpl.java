package com.sinse.universe.model.promotion;

import com.sinse.universe.domain.Promotion;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public List<Promotion> findByArtistId(int artistId) {
        return promotionRepository.findByArtist_Id(artistId);
    }

    @Override
    public Promotion select(int id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PROMOTION_NOT_FOUND));
    }

    @Override
    public void regist(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    @Override
    public void update(Promotion promotion) {
        Promotion existing = promotionRepository.findById(promotion.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PROMOTION_NOT_FOUND));

        existing.setName(promotion.getName());
        existing.setDescription(promotion.getDescription());
        existing.setImg(promotion.getImg());
        existing.setPrice(promotion.getPrice());
        existing.setFanOnly(promotion.isFanOnly());
        existing.setStockQty(promotion.getStockQty());
        existing.setLimitPerUser(promotion.getLimitPerUser());
        existing.setCoupon(promotion.getCoupon());
        existing.setArtist(promotion.getArtist());

        promotionRepository.save(existing);
    }

    @Override
    public void delete(int id) {
        Promotion existing = promotionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PROMOTION_NOT_FOUND));
        promotionRepository.delete(existing);
    }
}
