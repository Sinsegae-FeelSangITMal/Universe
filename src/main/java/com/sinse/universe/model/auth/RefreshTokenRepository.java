package com.sinse.universe.model.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    //refreshToken:<refreshToken> <userId>
    private static final String PREFIX = "refreshToken:";

    private final StringRedisTemplate redisTemplate;

    public void save(String token, String userId, Duration ttl){
        redisTemplate.opsForValue().set(PREFIX + token, userId, ttl);
    }

    public Optional<String> find(String token){
        return Optional.ofNullable(redisTemplate.opsForValue().get(PREFIX + token));
    }

    public void delete(String token) {
        redisTemplate.delete(PREFIX + token);
    }
}
