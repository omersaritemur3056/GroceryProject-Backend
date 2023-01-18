package com.example.grocery.core.security.DTOs.request;

import java.time.LocalDateTime;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 21)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,21}$")
    private String password;

    @Transient
    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDateTime = LocalDateTime.now();

    private boolean isActive = true;
}
