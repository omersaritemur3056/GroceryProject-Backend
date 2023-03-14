package com.example.grocery.service.rules;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.core.security.services.UserService;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.CorporateCustomerRepository;
import com.example.grocery.entity.concretes.CorporateCustomer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CorporateCustomerBusinessRules {

    @Autowired
    private CorporateCustomerRepository corporateCustomerRepository;
    @Autowired
    private UserService userService;

    public Result isExistTaxNumber(String taxNumber) {
        if (corporateCustomerRepository.existsByTaxNumber(taxNumber)) {
            log.warn(Messages.LogMessages.LogWarnMessages.TAX_NUMBER_REPEATED, taxNumber);
            throw new BusinessException(Messages.ErrorMessages.TAX_NUMBER_REPEATED);
        }
        return new SuccessResult();
    }

    public Result isExistId(Long id) {
        if (!userService.existById(id)) {
            throw new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public Result isExistUserId(Long userId) {
        if (corporateCustomerRepository.existsByUser_Id(userId)) {
            log.warn(Messages.LogMessages.LogWarnMessages.USER_ID_REPEATED, userId);
            throw new BusinessException(Messages.ErrorMessages.USER_ID_REPEATED);
        }
        return new SuccessResult();
    }

    public Result isExistImageId(Long imageId) {
        if (corporateCustomerRepository.existsByImage_Id(imageId)) {
            log.warn(Messages.LogMessages.LogWarnMessages.IMAGE_ID_REPEATED, imageId);
            throw new BusinessException(Messages.ErrorMessages.IMAGE_ID_REPEATED);
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
        CorporateCustomer checkField = new CorporateCustomer();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(Messages.ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }
}
