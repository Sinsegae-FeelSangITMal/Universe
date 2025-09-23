package com.sinse.universe.model.color;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Color;
import com.sinse.universe.model.artist.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {

    public final ColorRepository colorRepository;
    public final ArtistRepository artistRepository;

    public ColorServiceImpl(ColorRepository colorRepository, ArtistRepository artistRepository) {
        this.colorRepository = colorRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Color> selectAll() {
        return colorRepository.findAll();
    }

    @Override
    public Color select(int colorId) {
        return colorRepository.findById(colorId).orElse(null);
    }

    @Override
    public void regist(Color color) {
        colorRepository.save(color);
    }

    @Override
    public void update(Color color) {
        // DB에서 기존 Color 가져오기
        Color existing = colorRepository.findById(color.getId())
                .orElseThrow(() -> new RuntimeException("Color not found"));

        // 변경 가능한 필드만 update
        existing.setBgColor(color.getBgColor());

        // ✅ artist는 null로 덮어쓰지 않도록 체크
        if (color.getArtist() != null && color.getArtist().getId() > 0) {
            Artist artist = artistRepository.findById(color.getArtist().getId())
                    .orElseThrow(() -> new RuntimeException("Artist not found"));
            existing.setArtist(artist);
        }

        colorRepository.save(existing);
    }

    @Override
    public void delete(int colorId) {
        colorRepository.deleteById(colorId);
    }

    @Override
    public Color findByArtistId(int artistId) {
        return colorRepository.findByArtistId(artistId);
    }
}
