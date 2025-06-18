package org.example.auth.service;

import org.example.auth.dto.api.RefreshTokenDto;
import reactor.core.publisher.Mono;

public interface RefreshTokenService {

    Mono<Boolean> saveRefreshToken(RefreshTokenDto dto);

    Mono<Boolean> validateRefreshToken(RefreshTokenDto dto);

    Mono<Boolean> deleteRefreshToken(String userId);
}
