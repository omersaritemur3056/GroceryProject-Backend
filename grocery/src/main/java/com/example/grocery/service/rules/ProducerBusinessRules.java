package com.example.grocery.service.rules;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.ProducerRepository;
import com.example.grocery.entity.concretes.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerBusinessRules {

    @Autowired
    private ProducerRepository producerRepository;

    public Result isExistId(Long id) {
        if (!producerRepository.existsById(id)) {
            throw new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public Result isExistName(String name) {
        if (producerRepository.existsByNameIgnoreCase(name)) {
            log.warn(Messages.LogMessages.LogWarnMessages.PRODUCER_NAME_REPEATED, name);
            throw new BusinessException(Messages.ErrorMessages.PRODUCER_NAME_REPEATED);
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
        Producer checkField = new Producer();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(Messages.ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }
}
