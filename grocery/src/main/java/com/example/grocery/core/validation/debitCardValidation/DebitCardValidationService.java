package com.example.grocery.core.validation.debitCardValidation;

import com.example.grocery.core.utilities.results.Result;

public interface DebitCardValidationService {

    Result checkIfRealDebitCard(String cardNumber, String fullName, int cardExpirationYear, int cardExpirationMonth,
            String cardCvv);
}
