package org.example.auth.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRefreshTokenDto {
    private String platformType;// PC, Mobile 구분
}
