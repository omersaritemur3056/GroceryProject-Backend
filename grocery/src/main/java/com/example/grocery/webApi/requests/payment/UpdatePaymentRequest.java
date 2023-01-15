package com.example.grocery.webApi.requests.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    // pattern...
    private String cardNumber;

    @NotNull
    @NotBlank
    private String fullName;

    // yıl ve ay kısmına uğraş...
    private int cardExpirationYear;

    private int cardExpirationMonth;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    private String cardCvv;

    @Positive
    private double balance;
}
