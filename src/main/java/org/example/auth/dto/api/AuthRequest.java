package org.example.auth.dto.api;

import jakarta.validation.constraints.NotBlank;


public record AuthRequest(
        @NotBlank String userId,
        @NotBlank String password
){

}