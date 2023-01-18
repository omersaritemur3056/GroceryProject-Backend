package com.example.grocery.core.security.DTOs.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForLoginDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
