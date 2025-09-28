package com.sinse.universe.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StreamRequest {
    @NotBlank
    private String title;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm[:ss]")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm[:ss]")
    private LocalDateTime time;

    private boolean fanOnly;
    private boolean prodLink;   // SR_PROD_LINK
    private boolean prYn;       // SR_PR_YN

    private ArtistDto artist;   // { id: 1 }
    private PromotionDto promotion; // { id: 2 } or null
    private List<Integer> productIds; // [1,2,3]

    @Data
    public static class ArtistDto {
        private Integer id;
    }

    @Data
    public static class PromotionDto {
        private Integer id;
    }
}
