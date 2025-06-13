package org.example.auth.handler;

import org.example.common.dto.ApiResponseDto;
import org.example.common.exeption.ApiResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice(basePackages = "org.example.auth.controller")
public class AuthExceptionHandler {

    @ExceptionHandler(ApiResponseException.class)
    public Mono<ResponseEntity<ApiResponseDto>> handleApiResponseException(ApiResponseException ex) {
        return Mono.just(ResponseEntity.status(ex.getHttpStatus()).body(ex.getResponse()));
    }
}
