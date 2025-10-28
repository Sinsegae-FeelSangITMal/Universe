package com.sinse.universe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinse.universe.domain.Stream;
import com.sinse.universe.enums.StreamStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StreamResponse {

    private int id;
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private StreamStatus status;

    @JsonProperty("isAir")
    private boolean isAir;

    private boolean fanOnly;
    private boolean prodLink;
    private boolean prYn;

    private int viewCnt;
    private int likeCnt;

    private String thumb;

    private String record;

    private Integer artistId;
    private String artistName;
    private Integer promotionId;

    public static StreamResponse from(Stream stream) {
        if (stream == null) return null;
        return StreamResponse.builder()
                .id(stream.getId())
                .title(stream.getTitle())
                .time(stream.getTime())
                .endTime(stream.getEndTime())
                .status(stream.getStatus())
                .isAir(stream.isAir())
                .fanOnly(stream.isFanOnly())
                .prodLink(stream.isProdLink())
                .prYn(stream.isPrYn())
                .viewCnt(stream.getViewCnt() != null ? stream.getViewCnt() : 0)
                .likeCnt(stream.getLikeCnt() != null ? stream.getLikeCnt() : 0)
                .thumb(stream.getThumb())
                .record(stream.getRecord())
                .artistId(stream.getArtist() != null ? stream.getArtist().getId() : null)
                .artistName(stream.getArtist() != null ? stream.getArtist().getName() : null)
                .promotionId(stream.getPromotion() != null ? stream.getPromotion().getId() : null)
                .build();
    }
}
