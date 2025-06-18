package org.example.auth.service;

import org.example.auth.dto.model.MemberDto;
import org.example.common.consts.RedisIdentifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class MemberCacheServiceImpl implements MemberCacheService {

    private final Integer cacheTime;
    private final ReactiveRedisTemplate<String, MemberDto> redisTemplate;

    public MemberCacheServiceImpl(
            @Value("${spring.data.redis.cache-time}") Integer cacheTime,
            @Qualifier("redisMemberCache") ReactiveRedisTemplate<String, MemberDto> redisTemplate
    ) {
        this.cacheTime = cacheTime;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Boolean> saveMember(MemberDto dto) {
        return redisTemplate.opsForValue()
                .set(addIdentifier(dto.getUserId()),dto, Duration.ofMinutes(cacheTime));
    }

    @Override
    public Mono<MemberDto> getMember(String userId) {
        return redisTemplate.opsForValue().get(addIdentifier(userId));
    }

    @Override
    public Mono<Boolean> deleteMember(String userId) {
        return redisTemplate.delete(addIdentifier(userId)).map(count -> count > 0);
    }

    private String addIdentifier(String userId){
        StringBuilder sb = new StringBuilder()
                .append(RedisIdentifier.MEMBER)
                .append(userId);
        return sb.toString();
    }
}
