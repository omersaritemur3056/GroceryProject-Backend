package com.example.grocery.core.security.DTOs.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetByIdUserResponseDto {

    private Long id;

    private String username;

    private String email;

    private String password;

    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDateTime;

    private boolean isActive;
}
