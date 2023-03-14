package com.example.grocery.webApi.requests.payment;

import org.hibernate.validator.constraints.CreditCardNumber;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePaymentRequest {

    @NotBlank
    @NotNull
    @CreditCardNumber
    private String cardNumber;

    @NotNull
    @NotBlank
    private String fullName;

    @Min(value = 23)
    private int cardExpirationYear;

    @Min(value = 1)
    @Max(value = 12)
    private int cardExpirationMonth;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[0-9]{3}$")
    private String cardCvv;

    @PositiveOrZero
    private double balance;
}
