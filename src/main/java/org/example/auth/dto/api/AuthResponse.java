package org.example.auth.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        @JsonIgnore String refToken,
        String memberName,
        String localeCode
){

}
