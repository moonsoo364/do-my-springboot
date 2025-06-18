package org.example.auth.service;

import org.example.auth.dto.model.MemberDto;
import reactor.core.publisher.Mono;

public interface MemberCacheService {
    Mono<Boolean> saveMember(MemberDto dto);

    public Mono<MemberDto> getMember(String userId);

    public Mono<Boolean> deleteMember(String userId);
}
