package com.sinse.universe.model.artist;

import com.sinse.universe.dto.response.PartnerArtistResponse;
import com.sinse.universe.domain.Artist;
import com.sinse.universe.dto.request.ArtistRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArtistService {

    // 아티스트 전체 조회
    public List<Artist> selectAll();

    // 아티스트 1건 조회
    public Artist select(int artistId);

    // 아티스트 등록
    public void regist(ArtistRequest request);

    // 아티스트 수정
    public void update(Artist artist, MultipartFile mainImage, MultipartFile logoImage, boolean deleteMainImage, boolean deleteLogoImage) throws IOException;

    // 아티스트 삭제
    public void delete(int artistId);

    // 소속사(Partner) ID로 아티스트 정보 조회
    public List<Artist> findByPartnerId(int partnerId);

    // 소속사(Partner) ID로 아티스트 이름만 조회
    public List<PartnerArtistResponse> selectByPartnerId (int partnerId);
}
