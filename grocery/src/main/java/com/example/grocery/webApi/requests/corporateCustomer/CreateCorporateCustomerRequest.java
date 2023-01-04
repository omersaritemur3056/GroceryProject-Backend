package com.example.grocery.webApi.requests.corporateCustomer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateCorporateCustomerRequest {

    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,15}$")
    private String password;

    @NotBlank
    @NotNull
    private String address;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$")
    private String phoneNumber;

    @NotBlank
    @NotNull
    private String companyName;

    @Size(min = 10, max = 10)
    private String taxNumber;
}
