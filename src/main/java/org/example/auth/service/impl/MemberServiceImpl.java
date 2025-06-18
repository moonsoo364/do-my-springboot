package org.example.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auth.dao.MemberDao;
import org.example.auth.dto.model.MemberDto;
import org.example.auth.model.Member;
import org.example.auth.repository.MemberRepository;
import org.example.auth.service.MemberCacheService;
import org.example.auth.service.MemberService;
import org.example.common.consts.RedisIdentifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MemberDao memberDao;

    private final MemberCacheService memberCacheService;


    @Override
    public Mono<Member> registerMember(MemberDto dto){
        return memberRepository.findByUserId(dto.getUserId())
                .flatMap(existUser ->Mono.error(new IllegalArgumentException("UserId already Exists")))
                .switchIfEmpty(Mono.defer(()-> {
                    dto.setPassword(
                            passwordEncoder.encode(dto.getPassword())
                    );
                    return memberRepository.save(new Member(dto));
                })).cast(Member.class);
    }

    @Override
    public Mono<Member> findByUserId(String userId){
        return memberRepository.findByUserId(userId);
    }

    @Override
    public Mono<Boolean> existsById(String userId) {
        return memberRepository.existsById(userId);
    }

    @Override
    public Mono<Member> findUserByUserId(String userId) {
        return memberDao.findUserByUserId(userId);
    }

    //마리아 디비 조회
    @Override
    public Mono<MemberDto> findUserProjectionByUserId(String userId) {
        return memberDao.findUserProjectionByUserId(userId);
    }


    @Override
    public Mono<MemberDto> findUserByUserIdUseCache_2(String userId) {
        return memberCacheService.getMember(userId)
                .switchIfEmpty(
                        memberDao.findUserProjectionByUserId(userId)
                                .flatMap(member -> {
                                            if(member == null) return Mono.empty();// null cache 방지
                                            return memberCacheService.saveMember(member)
                                                    .thenReturn(member);
                                        }

                                ).onErrorResume(e ->
                                {
                                    return memberDao.findUserProjectionByUserId(userId);
                                })

                );
    }

    @Override
    public Mono<Integer> deleteByUserId(String userId) {
        return memberRepository.deleteByUserIdReturnCount(userId);
    }

    private String addIdentifier(String userId){
        StringBuilder sb = new StringBuilder()
                .append(RedisIdentifier.MEMBER)
                .append(userId);
        return sb.toString();
    }
}
