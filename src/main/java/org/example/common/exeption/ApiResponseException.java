package org.example.common.exeption;

import org.example.common.dto.ApiResponseDto;

public class ApiResponseException extends RuntimeException {
    private final ApiResponseDto response;

    public ApiResponseException(ApiResponseDto response) {
        super(response.getResultMsg());
        this.response = response;
    }

    public ApiResponseDto getResponse() {
        return response;
    }
}
