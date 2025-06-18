package org.example.auth.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDto {
    private String userId;
    private String refreshToken;
    private Duration duration;
}
