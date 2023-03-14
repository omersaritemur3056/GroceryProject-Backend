package com.example.grocery.api.responses.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllPaymentResponse {

    private Long id;

    private String cardNumber;

    private String fullName;

    private int cardExpirationYear;

    private int cardExpirationMonth;

    private String cardCvv;

    private double balance;
}
