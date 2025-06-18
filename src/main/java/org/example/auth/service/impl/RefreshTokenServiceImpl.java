package org.example.auth.service.impl;

import org.example.auth.dto.api.RefreshTokenDto;
import org.example.auth.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.example.common.consts.RedisIdentifier.*;


@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final Duration cacheTime;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public RefreshTokenServiceImpl(
            @Value("${jwt.ref-expiration}") Integer cacheTime,
            @Qualifier("refreshTokenTemplate") ReactiveRedisTemplate<String, String> redisTemplate
    ) {
        this.cacheTime = Duration.ofMinutes(cacheTime);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Boolean> saveRefreshToken(RefreshTokenDto dto) {
        return redisTemplate.opsForValue().set(
                addIdentifier(dto.getUserId(), REFRESH),
                dto.getRefreshToken(),
                cacheTime);
    }

    @Override
    public Mono<Boolean> validateRefreshToken(RefreshTokenDto dto) {
        return redisTemplate.opsForValue()
                .get(addIdentifier(dto.getUserId(), REFRESH))
                .map(storedToken -> storedToken.equals(dto.getRefreshToken()))
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Boolean> deleteRefreshToken(String userId) {
        return redisTemplate.delete(addIdentifier(userId, REFRESH))
                .map(deleted -> deleted > 0);
    }
}
