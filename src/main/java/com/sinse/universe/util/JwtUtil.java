package com.sinse.universe.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private SecretKey key;
    private String issuer;
    private Duration accessTokenTtl;
    @Getter
    private Duration refreshTokenTtl;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.issure}") String issuer,
            @Value("${app.jwt.access-ttl}") Duration accessTokenTtl,
            @Value("${app.jwt.refresh-ttl}") Duration refreshTokenTtl
    ){
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.accessTokenTtl = accessTokenTtl;
        this.refreshTokenTtl = refreshTokenTtl;
    }

    public String createAccessToken(int memberId, String roleName, String email) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessTokenTtl.toMillis());

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(String.valueOf(memberId))
                .setId(UUID.randomUUID().toString()) // 토큰 고유 ID
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("role", roleName)
                .claim("email", email)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(int member_id) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshTokenTtl.toMillis());

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(String.valueOf(member_id))
                .setId(UUID.randomUUID().toString()) // 토큰 고유 ID
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }

    // 검증
    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

}
