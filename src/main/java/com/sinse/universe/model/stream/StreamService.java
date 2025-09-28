package com.sinse.universe.model.stream;

import com.sinse.universe.domain.Stream;
import com.sinse.universe.dto.request.StreamRequest;

import java.util.List;

public interface StreamService {
    public List<Stream> selectAll();
    public Stream select(int streamId);;
    public int regist(StreamRequest request);
    public void update(int streamId, StreamRequest request);
    public void delete(int streamId);
    public List<Stream> findByArtistId(int streamId);
}
