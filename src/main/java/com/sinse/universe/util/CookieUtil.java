package com.sinse.universe.util;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {

    public static ResponseCookie setResponseCookie(String token, Duration maxAge){
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
