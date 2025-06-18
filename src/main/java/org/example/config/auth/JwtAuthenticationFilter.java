package org.example.config.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.dao.MemberDao;
import org.example.auth.jwt.JwtUtil;
import org.example.auth.model.Member;
import org.example.auth.service.MemberService;
import org.example.common.consts.ResultCode;
import org.example.common.dto.ApiResponseDto;
import org.example.common.exeption.ApiResponseException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.rmi.server.ExportException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    //private final MemberDao memberDao;
    private final MemberService memberService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(context -> {
                    log.info("## SecurityContext exists :  = {}", (Member)context.getAuthentication().getPrincipal());
                    return chain.filter(exchange);
                })
                .switchIfEmpty(Mono.defer(()-> {
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        String userId = jwtUtil.getUsernameFromToken(token);

                        return memberService.findUserProjectionByUserId(userId)
                                .filter(user -> jwtUtil.validateToken(token, userId))
                                .map(Member::new)
                                .flatMap(user -> {
                                    Authentication auth = new UsernamePasswordAuthenticationToken(
                                            user, null, user.getAuthorities());
                                    SecurityContext context = new SecurityContextImpl(auth);
                                    return chain.filter(exchange)
                                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
                                });

                    }
                    return chain.filter(exchange);
                }))
                .onErrorResume(ExpiredJwtException.class, e -> {
                    log.warn("JWT expired: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    exchange.getResponse().getHeaders().add("Content-Type", "application/json");

                    ApiResponseDto response = new ApiResponseDto(
                            HttpStatus.BAD_REQUEST,
                            ResultCode.BAD_REQUEST,
                            "Login session is expired, login please"
                    );

                    byte[] bytes = null;
                    try {
                        bytes = new ObjectMapper().writeValueAsBytes(response);
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                });
        }
}
