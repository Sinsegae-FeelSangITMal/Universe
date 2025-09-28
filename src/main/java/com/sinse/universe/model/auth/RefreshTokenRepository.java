package com.sinse.universe.model.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private static final String PREFIX = "refreshToken:";

    private final StringRedisTemplate redisTemplate;

    public void save(String userId, String token, Duration ttl){
        redisTemplate.opsForValue().set(PREFIX + userId, token, ttl);
    }

    public Optional<String> find(String userId){
        return Optional.ofNullable(redisTemplate.opsForValue().get(PREFIX + userId));
    }

    public void delete(String userId) {
        redisTemplate.delete(PREFIX + userId);
    }
}
