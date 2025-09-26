package com.sinse.universe.util;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {

    public static ResponseCookie getResponseCookie(String token, Duration maxAge){
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
