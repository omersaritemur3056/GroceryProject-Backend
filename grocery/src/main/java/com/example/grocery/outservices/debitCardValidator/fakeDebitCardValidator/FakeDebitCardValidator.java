package com.example.grocery.outservices.debitCardValidator.fakeDebitCardValidator;

import org.springframework.stereotype.Service;

@Service
public class FakeDebitCardValidator {

    // fake validator api...
    public boolean cardValidate(String cardNumber, String fullName, int cardExpirationYear,
            int cardExpirationMonth, String cardCvv) {
        return true;
    }
}
