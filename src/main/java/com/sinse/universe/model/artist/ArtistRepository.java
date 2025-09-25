package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    public List<Artist> findByPartner_Id (Integer partnerId);
}
