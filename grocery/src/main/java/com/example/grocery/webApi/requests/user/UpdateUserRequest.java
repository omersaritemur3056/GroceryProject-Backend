package com.example.grocery.webApi.requests.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// import javax.validation.constraints.Email;
// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.NotNull;
// import javax.validation.constraints.Pattern;
// import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,15}$")
    private String password;
}
