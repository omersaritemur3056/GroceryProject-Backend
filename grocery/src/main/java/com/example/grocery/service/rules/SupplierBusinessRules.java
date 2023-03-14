package com.example.grocery.service.rules;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.repository.SupplierRepository;
import com.example.grocery.model.concretes.Supplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SupplierBusinessRules {

    private final SupplierRepository supplierRepository;

    public Result isExistId(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public Result isExistName(String name) {
        if (supplierRepository.existsByName(name)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SUPPLIER_NAME_REPEATED, name);
            throw new BusinessException(Messages.ErrorMessages.SUPPLIER_NAME_REPEATED);
        }
        return new SuccessResult();
    }

    public Result isExistEmail(String email) {
        if (supplierRepository.existsByEmail(email)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SUPPLIER_EMAIL_REPEATED, email);
            throw new BusinessException(Messages.ErrorMessages.EMAIL_REPEATED);
        }
        return new SuccessResult();
    }

    public Result isExistPhoneNumber(String phoneNumber) {
        if (supplierRepository.existsByPhoneNumber(phoneNumber)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SUPPLIER_PHONE_NUMBER_REPEATED, phoneNumber);
            throw new BusinessException(Messages.ErrorMessages.PHONE_NUMBER_REPEATED);
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
        Supplier checkField = new Supplier();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(Messages.ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }
}
