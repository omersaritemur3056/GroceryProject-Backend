package com.example.grocery.core.security.DTOs.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllUserResponseDto {

    private Long id;

    private String username;

    private String email;

    private String password;

    private List<String> roles;

    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDateTime;

    private boolean isActive;
}
