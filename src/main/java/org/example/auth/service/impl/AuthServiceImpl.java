package org.example.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auth.dto.api.AuthRequest;
import org.example.auth.dto.api.AuthResponse;
import org.example.auth.dto.model.MemberDto;
import org.example.auth.jwt.JwtUtil;
import org.example.auth.repository.MemberRepository;
import org.example.auth.service.AuthService;
import org.example.common.consts.TokenType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<AuthResponse> authenticate(AuthRequest authRequest) {
        return memberRepository.findByUserId(authRequest.userId())
                .filter(member -> passwordEncoder.matches(
                        authRequest.password(),
                        member.getPassword()))
                .map(member -> AuthResponse.builder()
                        .token(jwtUtil.generateToken(new MemberDto(member, TokenType.REFRESH)))
                        .memberName(member.getMemberName())
                        .localeCode(member.getLocaleCode())
                        .build()
                        )
                .switchIfEmpty(Mono.error(new BadCredentialsException("InValid credentials")));
    }
}
