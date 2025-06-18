package org.example.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.dto.api.AuthRequest;
import org.example.auth.dto.api.AuthResponse;
import org.example.auth.dto.api.CheckUserDto;
import org.example.auth.dto.model.MemberDto;
import org.example.auth.service.AuthService;
import org.example.auth.service.MemberCacheService;
import org.example.auth.service.MemberService;
import org.example.common.consts.ResultCode;
import org.example.common.dto.ApiResponseDto;
import org.example.common.exeption.ApiResponseException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final MemberCacheService memberCacheService;

    @PostMapping("/login")
    public Mono<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest){
        return authService.authenticate(authRequest)
                .onErrorMap(
                        Exception.class,
                        e-> new ApiResponseException(new ApiResponseDto(
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



}
