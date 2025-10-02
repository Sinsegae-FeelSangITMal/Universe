package com.sinse.universe.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

@Data
public class StreamRequest {
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")  // ← datetime-local 포맷
    private LocalDateTime time;

    private Boolean fanOnly;
    private Boolean prodLink;
    private Boolean prYn;

    private Integer artistId;
    private Integer promotionId;

    private MultipartFile thumb;

    private Boolean deleteThumb;
}
