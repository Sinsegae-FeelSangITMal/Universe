package com.sinse.universe.model.stream;

import com.sinse.universe.domain.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StreamRepository extends JpaRepository<Stream, Integer> {
    //    List<Stream> findByArtistId(Integer id);
    Page<Stream> findByArtistId(Integer artistId, Pageable pageable);
    List<Stream> findByArtistId(Integer artistId);
}
