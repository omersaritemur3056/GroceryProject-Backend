package com.example.grocery.core.security.DTOs.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequest {

    @NotBlank
    private String refreshToken;
}
