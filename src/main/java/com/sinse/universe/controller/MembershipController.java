package com.sinse.universe.controller;

import com.sinse.universe.domain.Membership;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.MembershipResponse;
import com.sinse.universe.dto.response.OrderForEntResponse;
import com.sinse.universe.model.membership.MembershipRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {
    private final MembershipRepository membershipRepository;
    public MembershipController(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<MembershipResponse>>> getMemberships(@PathVariable int userId) {
        return ApiResponse.success("멤버십 목록 조회",
                membershipRepository.findAllByUser_Id(userId).stream()
                .map(MembershipResponse::from)
                .toList());
    }
}
