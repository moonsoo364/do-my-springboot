package org.example.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.dto.api.*;
import org.example.auth.dto.model.MemberDto;
import org.example.auth.jwt.JwtUtil;
import org.example.auth.service.AuthService;
import org.example.auth.service.MemberCacheService;
import org.example.auth.service.MemberService;
import org.example.auth.service.RefreshTokenService;
import org.example.common.consts.ResultCode;
import org.example.common.dto.ApiResponseDto;
import org.example.common.exeption.ApiResponseException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final MemberCacheService memberCacheService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Mono<AuthResponse> login(
            @Valid @RequestBody AuthRequest authRequest,
            ServerHttpRequest serverRequest,      // ✅ 이름 중복 피함
            ServerHttpResponse serverResponse
    ) {
        String platformType = authService.extractDeviceInfo(serverRequest);

        return authService.authenticate(authRequest, platformType)
                .flatMap(auth -> {
                    ResponseCookie cookie = ResponseCookie
                            .from("refreshToken", auth.refToken())
                            .httpOnly(true)
                            .sameSite("Strict")
                            .maxAge(Duration.ofDays(1))
                            .path("/")
                            .build();

                    serverResponse.addCookie(cookie);

                    return Mono.just(auth); // 이건 그대로 반환하면 AccessToken 포함된 JSON 본문
                })
                .onErrorMap(Exception.class, e ->
                        new ApiResponseException(new ApiResponseDto(
                                HttpStatus.UNAUTHORIZED,
                                ResultCode.FAIL,
                                "Fail, user login"
                        ))
                );
    }


    @PostMapping("/register")
    public Mono<ApiResponseDto> register(
            @Valid @RequestBody MemberDto dto
            ){
        dto.setNewMember(true);
        return memberService.registerMember(dto)
                .map(exists ->new ApiResponseDto(
                        ResultCode.SUCCESS,
                        "User registered successfully"))
                .onErrorMap( e->{
                    if(e instanceof IllegalArgumentException
                        || e instanceof  DuplicateKeyException){
                        return new ApiResponseException(new ApiResponseDto(
                                HttpStatus.BAD_REQUEST,
                                ResultCode.BAD_REQUEST,
                                "Fail userId already exists"));
                    }
                    return new ApiResponseException(new ApiResponseDto());
                });
    }

    @GetMapping("/check/user/id")
    public Mono<CheckUserDto> checkUserId(@RequestParam(required = true) String userId){
        return memberService.findUserProjectionByUserId(userId).flatMap(
                member -> {
            return Mono.just(new CheckUserDto(member));
        });
    }

    @DeleteMapping("/user/session")
    public Mono<ApiResponseDto> deleteSession(@RequestParam(required = true) String userId){
        return memberCacheService.deleteMember(userId).map(result ->{
            if(result){
                return new ApiResponseDto(ResultCode.SUCCESS,"User session is deleted");
            }
            return new ApiResponseDto(ResultCode.FAIL,"User session not found or already deleted");
        });
    }

    @PostMapping("/refresh")
    public Mono<AuthResponse> refreshToken(@RequestBody RefreshTokenDto request) {
        String userId = request.getUserId();
        String refreshToken = request.getRefreshToken();

        return refreshTokenService.validateRefreshToken(request)
                .flatMap(valid -> {
                    if (!valid) {
                        throw new ApiResponseException(new ApiResponseDto(
                                HttpStatus.UNAUTHORIZED,
                                ResultCode.UNAUTHORIZED,
                                "RefreshToken is invalid"
                        ));
                    }

                    // Redis에 있는 리프레시 토큰이 유효하면 새로운 Access Token 발급
                    return memberService.findUserByUserIdUseCache_2(userId)
                            .map(dto -> {
                                    String newAccess = jwtUtil.generateToken(dto);
                                    return AuthResponse.builder()
                                            .token(newAccess)
                                            .memberName(dto.getMemberName())
                                            .localeCode(dto.getLocaleCode())
                                            .build();
                            });

                });
    }


}
