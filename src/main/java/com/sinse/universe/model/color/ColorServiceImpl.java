package com.sinse.universe.model.color;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Color;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
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
        return colorRepository.findById(colorId)
                .orElseThrow(() -> new CustomException(ErrorCode .COLOR_NOT_FOUND) );
    }

    @Override
    public void regist(Color color) {
        colorRepository.save(color);
    }

    @Override
    public void update(Color color) {
        Color existing = colorRepository.findById(color.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.COLOR_NOT_FOUND));

        // 변경 가능한 필드만 업데이트
        existing.setBgColor(color.getBgColor());

        // Artist 갱신 (null/잘못된 ID 방지)
        if (color.getArtist() != null
                && color.getArtist().getId() > 0) {
            Artist artist = artistRepository.findById(color.getArtist().getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ARTIST_NOT_FOUND));
            existing.setArtist(artist);
        }

        colorRepository.save(existing);
    }

    @Override
    public void delete(int colorId) {
        Color existing = colorRepository.findById(colorId)
                .orElseThrow(() -> new CustomException(ErrorCode.COLOR_NOT_FOUND));
        colorRepository.delete(existing);
    }

    @Override
    public Color findByArtistId(int artistId) {
        return colorRepository.findByArtistId(artistId);
        // 컬러가 없으면 null 그대로 반환 → 예외 던지지 않음
    }
}
