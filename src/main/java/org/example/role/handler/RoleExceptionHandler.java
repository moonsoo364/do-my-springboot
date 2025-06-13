package org.example.role.handler;

import org.example.common.dto.ApiResponseDto;
import org.example.common.exeption.ApiResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import reactor.core.publisher.Mono;

@RestControllerAdvice(basePackages = "org.example.role.controller")
public class RoleExceptionHandler {

    @ExceptionHandler(ApiResponseException.class)
    public Mono<ResponseEntity<ApiResponseDto>> handleApiResponseException(ApiResponseException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getResponse()));
    }
}
