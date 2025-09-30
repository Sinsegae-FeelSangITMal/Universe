package com.sinse.universe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/hello")
    public ResponseEntity<String> getHello() {
        return ResponseEntity.ok("재발급 성공");
    }
}
