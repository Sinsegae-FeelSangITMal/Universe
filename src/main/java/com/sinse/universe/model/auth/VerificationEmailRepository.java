package com.sinse.universe.model.auth;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class VerificationEmailRepository {

    private final StringRedisTemplate redisTemplate;  // <String, String>
    private static final String CODE_PREFIX = "verification:email-code:";
    private static final String VERIFIED_EMAIL_PREFIX = "verified:email:";
    public VerificationEmailRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveCode(String email, String code, Duration ttl){
        redisTemplate.opsForValue().set(CODE_PREFIX + email, code, ttl);
    }

    public Optional<String> getCode(String email){
        return Optional.ofNullable(redisTemplate.opsForValue().get(CODE_PREFIX + email));
    }

    public void saveVerifiedEmail(String email, Duration ttl) {
        redisTemplate.opsForValue().set(VERIFIED_EMAIL_PREFIX + email, "true", ttl);
    }
}
