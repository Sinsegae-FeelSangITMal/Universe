package com.sinse.universe.model.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private static final String PREFIX = "kakao:payment:";   //kakao:payment:{status}:{orderId}
    private final StringRedisTemplate redisTemplate;

    public void save(String orderId, String tid, Duration ttl){
        redisTemplate.opsForValue().set(PREFIX + orderId, tid, ttl);
    }
    public String get(String orderId){
        return redisTemplate.opsForValue().get(PREFIX + orderId);
    }
}
