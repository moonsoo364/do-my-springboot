package org.example.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.dto.api.AuthRequest;
import org.example.auth.dto.api.AuthResponse;
import org.example.auth.dto.api.CheckUserDto;
import org.example.auth.dto.model.MemberDto;
import org.example.auth.service.AuthService;
import org.example.auth.service.MemberService;
import org.example.common.consts.ResultCode;
import org.example.common.dto.ApiResponseDto;
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

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody AuthRequest authRequest){
        return authService.authenticate(authRequest)
                .map(ResponseEntity::ok)
                .onErrorReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<ApiResponseDto>> register(
            @Valid @RequestBody MemberDto dto
            ){
        dto.setNewMember(true);
        return memberService.registerMember(dto)
                .map(member -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(
                                new ApiResponseDto(
                                        ResultCode.SUCCESS,"User registered successfully!")
                        ))
                .onErrorResume(IllegalArgumentException.class, e-> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(
                                new ApiResponseDto(ResultCode.BAD_REQUEST,"check your member request")
                        ))
                );
    }

    @GetMapping("/check/user/id")
    public Mono<CheckUserDto> checkUserId(@RequestParam(required = true) String userId){
        return memberService.findUserProjectionByUserId(userId).flatMap(
                member -> {
            return Mono.just(new CheckUserDto(member));
        });
    }



}
