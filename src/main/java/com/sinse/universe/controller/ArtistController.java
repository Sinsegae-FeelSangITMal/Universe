package com.sinse.universe.controller;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.model.artist.ArtistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ArtistController {

    private ArtistService artistService;
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/artists/{partnerId}")
    public ResponseEntity<List<Map<String, Object>>> findAllArtistsByPartner(@PathVariable Integer partnerId) {
        return ResponseEntity.ok(artistService.findAllArtistNameAndIdByPartner(partnerId));
    }
}
