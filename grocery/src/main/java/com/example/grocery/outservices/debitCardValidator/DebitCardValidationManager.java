package com.example.grocery.outservices.debitCardValidator;

import org.springframework.stereotype.Service;

import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.core.validation.debitCardValidation.DebitCardValidationService;
import com.example.grocery.outservices.debitCardValidator.fakeDebitCardValidator.FakeDebitCardValidator;

@Service
public class DebitCardValidationManager implements DebitCardValidationService {

    public final FakeDebitCardValidator fakeDebitCardValidator;

    public DebitCardValidationManager(FakeDebitCardValidator fakeDebitCardValidator) {
        this.fakeDebitCardValidator = fakeDebitCardValidator;
    }

    // simulate validation
    @Override
    public Result checkIfRealDebitCard(String cardNumber, String fullName, int cardExpirationYear,
            int cardExpirationMonth, String cardCvv) {
        if (!fakeDebitCardValidator.cardValidate(cardNumber, fullName, cardExpirationYear, cardExpirationMonth,
                cardCvv)) {
            throw new BusinessException(ErrorMessages.DEBIT_CARD_NOT_VALID);
        }

        return new SuccessResult();
    }

}
