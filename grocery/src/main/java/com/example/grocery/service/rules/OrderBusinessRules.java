package com.example.grocery.service.rules;

import com.example.grocery.service.interfaces.PaymentService;
import com.example.grocery.service.interfaces.CustomerService;
import com.example.grocery.service.constants.Messages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderBusinessRules {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final PaymentService paymentService;

    public Result isExistId(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public Result isExistCustomerId(Long customerId) {
        if (customerService.getCustomerById(customerId) == null) {
            throw new BusinessException(Messages.ErrorMessages.CUSTOMER_ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public Result isExistPaymentId(Long paymentId) {
        if (paymentService.getPaymentById(paymentId) == null) {
            throw new BusinessException(Messages.ErrorMessages.PAYMENT_ID_NOT_FOUND);
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
}
