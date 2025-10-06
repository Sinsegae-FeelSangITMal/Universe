package com.sinse.universe.model.stream;

import com.sinse.universe.domain.Stream;
import com.sinse.universe.dto.request.StreamRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface StreamService {
    public List<Stream> selectAll();
    public Stream select(int streamId);
    public Stream regist(StreamRequest request) throws IOException;
    public Stream update(int id, StreamRequest request) throws IOException;
    public void delete(int streamId);
   public List<Stream> findByArtistId(int artistId);
    public Page<Stream> findByArtistId(int artistId, Pageable pageable);
}
