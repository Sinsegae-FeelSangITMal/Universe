package com.sinse.universe.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Getter
@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.issure}")
    private String issure;

    @Value("${app.jwt.access-ttl}")
    private Duration accessTokenTtl;

    @Getter
    @Value("${app.jwt.refresh-ttl}")
    private Duration refreshTokenTtl;

    public String createAccessToken(int memberId, String roleName) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessTokenTtl.toMillis());

        return Jwts.builder()
                .setIssuer(issure)
                .setSubject(String.valueOf(memberId))
                .setId(UUID.randomUUID().toString()) // 토큰 고유 ID
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("role", roleName)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String createRefreshToken(int member_id) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshTokenTtl.toMillis());

        return Jwts.builder()
                .setIssuer(issure)
                .setSubject(String.valueOf(member_id))
                .setId(UUID.randomUUID().toString()) // 토큰 고유 ID
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }


//
//    // jwt payload 조회
//    public String getRole(String token) {
//        return getClaims(token).get("role", String.class);
//    }
//
//
//    private Claims getClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }



}
