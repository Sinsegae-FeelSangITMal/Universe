package com.sinse.universe.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;

    @Test
    void accesstoken_유효시간확인() {

        //assertEquals(Duration.ofMinutes(15), jwtUtil.getACCESS_TOKEN_TTL());
    }

    @Test
    void refreshtoken_유효시간확인() {
        //assertEquals(Duration.ofDays(7), jwtUtil.getREFRESH_TOKEN_TTL());
    }

}