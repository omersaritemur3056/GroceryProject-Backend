package com.example.grocery.business.rules;

import com.example.grocery.business.abstracts.CustomerService;
import com.example.grocery.business.abstracts.PaymentService;
import com.example.grocery.business.constants.Messages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.OrderRepository;
import com.example.grocery.entity.concretes.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderBusinessRules {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PaymentService paymentService;

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

    public void isValidSortParameter(String sortBy) {
        Order checkField = new Order();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(Messages.ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }
}
