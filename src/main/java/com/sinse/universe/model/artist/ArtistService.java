package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;

import java.util.List;

public interface ArtistService {

    public List<Artist> selectAll();
    public Artist select(int artistId);
    public void regist(Artist artist);
    public void update(Artist artist);
    public void delete(int artistId);
    public List<Artist> findByPartnerId(int partnerId);
}
