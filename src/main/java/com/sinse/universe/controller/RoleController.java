package com.sinse.universe.controller;

import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.RoleResponse;
import com.sinse.universe.model.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

//    @GetMapping("/api/admin/roles")
//    public ResponseEntity<ApiResponse<List<RoleResponse>>> getRoles() {
//        return roleRepository.findAll();
//    }
}
