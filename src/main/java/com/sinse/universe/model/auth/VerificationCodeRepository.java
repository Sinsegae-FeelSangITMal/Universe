package com.sinse.universe.model.auth;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class VerificationCodeRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "auth:email:verification:";

    public VerificationCodeRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveCode(String email, String code, Duration ttl){
        redisTemplate.opsForValue().set(PREFIX + email, code, ttl);
    }
}
