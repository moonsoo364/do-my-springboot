package org.example.auth.service;

import org.example.auth.dto.api.AuthRequest;
import org.example.auth.dto.api.AuthResponse;
import org.example.auth.dto.api.RegisterRefreshTokenDto;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface AuthService {

    public Mono<AuthResponse> authenticate(AuthRequest authRequest, String refDto);

    public String extractDeviceInfo(ServerHttpRequest request);
}
