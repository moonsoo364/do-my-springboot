package org.example.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auth.dto.api.AuthRequest;
import org.example.auth.dto.api.AuthResponse;
import org.example.auth.dto.api.RefreshTokenDto;
import org.example.auth.dto.api.RegisterRefreshTokenDto;
import org.example.auth.dto.model.MemberDto;
import org.example.auth.jwt.JwtUtil;
import org.example.auth.repository.MemberRepository;
import org.example.auth.service.AuthService;
import org.example.auth.service.RefreshTokenService;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
    private final RefreshTokenService refreshTokenService;

    @Override
    public Mono<AuthResponse> authenticate(AuthRequest authRequest, String platformType) {
        return memberRepository.findByUserId(authRequest.userId())
                .filter(member -> passwordEncoder.matches(
                        authRequest.password(),
                        member.getPassword()))
                .map(member -> {
                    MemberDto memberDto = new MemberDto(member);
                    String accessToken = jwtUtil.generateToken(memberDto);
                    String refToken = jwtUtil.generateToken(memberDto, platformType);
                    refreshTokenService.saveRefreshToken(new RefreshTokenDto(member.getUserId(),refToken,platformType));

                    return AuthResponse.builder()
                            .token(accessToken)
                            .refToken(refToken)
                            .memberName(member.getMemberName())
                            .localeCode(member.getLocaleCode())
                            .build();
                })
                .switchIfEmpty(Mono.error(new BadCredentialsException("InValid credentials")));
    }

    @Override
    public String extractDeviceInfo(ServerHttpRequest request) {
        String userAgent = request.getHeaders().getFirst("User-Agent");

        String platformType = "PC";

        if (userAgent != null) {
            String ua = userAgent.toLowerCase();
            if (ua.contains("android") || ua.contains("iphone") || ua.contains("ipad")) {
                platformType = "Mobile";
            } else if (ua.contains("windows") || ua.contains("macintosh") || ua.contains("linux")) {
                platformType = "PC";
            }
        }

        return platformType;
    }
}
