package org.example.common.exeption;

import org.example.common.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;

public class ApiResponseException extends RuntimeException {
    private final ApiResponseDto response;

    public ApiResponseException(ApiResponseDto response) {
        super(response.getResultMsg());
        this.response = response;
    }

    public ApiResponseDto getResponse() {
        return response;
    }

    public HttpStatus getHttpStatus(){
        return response.getHttpStatus();
    }
}
