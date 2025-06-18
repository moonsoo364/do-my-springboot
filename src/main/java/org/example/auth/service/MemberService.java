package org.example.auth.service;

import org.example.auth.dto.model.MemberDto;
import org.example.auth.model.Member;
import reactor.core.publisher.Mono;

public interface MemberService {
    Mono<Member> registerMember(MemberDto dto);

    Mono<Member> findByUserId(String userId);

    Mono<Boolean> existsById(String userId);

    Mono<Member> findUserByUserId(String userId);

    Mono<MemberDto> findUserProjectionByUserId(String userId);

    Mono<Integer> deleteByUserId(String userId);

    Mono<MemberDto> findUserByUserIdUseCache(String userId);
}
