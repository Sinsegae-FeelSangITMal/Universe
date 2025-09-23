package com.sinse.universe.controller;

import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.PartnerArtistResponse;
import com.sinse.universe.model.artist.ArtistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class MainController {

    private final ArtistService artistService;
    public MainController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/")
    public int getUser() {
        return 10;
    }

    @GetMapping("/api/ent/artists/{partnerId}")
    public ResponseEntity<ApiResponse<List<PartnerArtistResponse>>> getArtists(@PathVariable int partnerId) {
        return ApiResponse.success("한 소속사의 아티스트 목록", artistService.selectByPartnerId(partnerId));
    }

}
