package com.sinse.universe.controller;

import com.sinse.universe.domain.Partner;
import com.sinse.universe.dto.response.ArtistResponse;
import com.sinse.universe.dto.response.PartnerResponse;
import com.sinse.universe.model.partner.PartnerRepository;
import com.sinse.universe.model.partner.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PartnerController {

    private final PartnerService partnerService;
    private final PartnerRepository partnerRepository;

    public PartnerController(PartnerService partnerService, PartnerRepository partnerRepository) {
        this.partnerService = partnerService;
        this.partnerRepository = partnerRepository;
    }

    // 전체 소속사
    @GetMapping("/partners")
    public List<PartnerResponse> getArtists() {
        return partnerRepository.findAll()
                .stream()
                .map(PartnerResponse::from)
                .toList();
    }

    // 1개 소속사 상세
    @GetMapping("/partners/{partnerId}")
    public PartnerResponse getPartner(@PathVariable int partnerId) {
        return PartnerResponse.from(partnerService.select(partnerId));
    }

    // 소속사 등록
    @PostMapping("/partners")
    public ResponseEntity<?> addPartner(@RequestBody Partner partner) {
        partnerService.regist(partner);
        return ResponseEntity.ok(Map.of("result", "소속사 등록 성공"));
    }

    // 소속사 수정
    @PutMapping("/partners/{partnerId}")
    public ResponseEntity<?> updatePartner(@RequestBody Partner partner, @PathVariable int partnerId) {
        partnerService.update(partner);
        return ResponseEntity.ok(Map.of("result", "소속사 수정 성공"));
    }

    // 소속사 삭제
    @DeleteMapping("/partners/{partnerId}")
    public ResponseEntity<?> deletePartner(@PathVariable int partnerId) {
        partnerService.delete(partnerId);
        return ResponseEntity.ok(Map.of("result", "소속사 삭제 성공"));
    }

}
