package com.sinse.universe.model.color;

import com.sinse.universe.domain.Color;

import java.util.List;

public interface ColorService {
    public List<Color> selectAll();
    public Color select(int colorId);
    public void regist(Color color);
    public void update(Color color);
    public void delete(int colorId);
    public Color findByArtistId(int artistId);
}