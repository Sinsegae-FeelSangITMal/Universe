package com.sinse.universe.model.stream;

import com.sinse.universe.domain.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StreamRepository extends JpaRepository<Stream,Integer> {

    List<Stream> findByArtistId(Integer id);
}
