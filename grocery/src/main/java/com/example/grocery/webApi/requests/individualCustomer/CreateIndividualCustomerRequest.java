package com.example.grocery.webApi.requests.individualCustomer;

import com.example.grocery.entity.enums.Gender;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateIndividualCustomerRequest {

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
    private String firstName;

    @NotBlank
    @NotNull
    private String lastName;

    @NotNull
    private Gender gender;

    @Size(min = 11, max = 11)
    @Pattern(regexp = "^[1-9][0-9]{10}$")
    private String nationalIdentity;

    @Min(value = 1)
    private Long userId;

    @Min(value = 1)
    private Long imageId;
}
