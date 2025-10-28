package com.sinse.universe.model.promotion;

import com.sinse.universe.domain.Promotion;

import java.util.List;

public interface PromotionService {
    public List<Promotion> findByArtistId(int artistId);

    public Promotion select(int id);

    public void regist(Promotion promotion);

    public void update(Promotion promotion);

    public void delete(int id);
}
