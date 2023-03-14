package com.example.grocery.service.rules;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.core.validation.debitCardValidation.DebitCardValidationService;
import com.example.grocery.repository.PaymentRepository;
import com.example.grocery.model.concretes.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentBusinessRules {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private DebitCardValidationService debitCardValidationService;

    public Result isValidCard(String cardNumber, String fullName, int cardExpirationYear, int cardExpirationMonth,
                               String cardCvv) {
        if (!debitCardValidationService
                .checkIfRealDebitCard(cardNumber, fullName, cardExpirationYear, cardExpirationMonth, cardCvv)
                .isSuccess()) {
            log.warn(Messages.LogMessages.LogWarnMessages.DEBIT_CARD_NOT_VALID, cardNumber, fullName, cardExpirationYear,
                    cardExpirationMonth, cardCvv);
            throw new BusinessException(Messages.ErrorMessages.DEBIT_CARD_NOT_VALID);
        }
        return new SuccessResult();
    }

    public Result isExistId(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public void isPageNumberValid(int pageNo) {
        if (pageNo < 0) {
            log.warn(Messages.LogMessages.LogWarnMessages.PAGE_NUMBER_NEGATIVE);
            throw new BusinessException(Messages.ErrorMessages.PAGE_NUMBER_NEGATIVE);
        }
    }

    public void isPageSizeValid(int pageSize) {
        if (pageSize < 1) {
            log.warn(Messages.LogMessages.LogWarnMessages.PAGE_SIZE_NEGATIVE);
            throw new BusinessException(Messages.ErrorMessages.PAGE_SIZE_NEGATIVE);
        }
    }

    public void isValidSortParameter(String sortBy) {
        Payment checkField = new Payment();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(Messages.ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }
}
