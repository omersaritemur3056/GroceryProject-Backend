package com.example.grocery.service.rules;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.core.validation.mernisValidation.MernisValidationService;
import com.example.grocery.repository.EmployeeRepository;
import com.example.grocery.model.concretes.Employee;
import com.example.grocery.model.enums.Nationality;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeBusinessRules {

    private final EmployeeRepository employeeRepository;
    private final MernisValidationService mernisValidationService;

    public Result isExistId(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public Result isExistNationalId(String nationalId) {
        if (employeeRepository.existsByNationalIdentity(nationalId)) {
            log.warn(Messages.LogMessages.LogWarnMessages.NATIONAL_IDENTITY_REPEATED, nationalId);
            throw new BusinessException(Messages.ErrorMessages.NATIONAL_IDENTITY_REPEATED);
        }
        return new SuccessResult();
    }

    public Result isExistUserId(Long userId) {
        if (employeeRepository.existsByUser_Id(userId)) {
            log.warn(Messages.LogMessages.LogWarnMessages.USER_ID_REPEATED, userId);
            throw new BusinessException(Messages.ErrorMessages.USER_ID_REPEATED);
        }
        return new SuccessResult();
    }

    public Result isExistImageId(Long imageId) {
        if (employeeRepository.existsByImage_Id(imageId)) {
            log.warn(Messages.LogMessages.LogWarnMessages.IMAGE_ID_REPEATED, imageId);
            throw new BusinessException(Messages.ErrorMessages.IMAGE_ID_REPEATED);
        }
        return new SuccessResult();
    }

    public Result isPermissibleAge(LocalDate birthYear) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthYear, today);
        int years = period.getYears();
        if (years < 18) {
            log.warn(Messages.LogMessages.LogWarnMessages.AGE_NOT_PERMISSIBLE, birthYear);
            throw new BusinessException(Messages.ErrorMessages.AGE_NOT_PERMISSIBLE);
        }
        return new SuccessResult();
    }

    public Result isTurkishCitizen(Employee employee) {
        if (mernisValidationService.validate(employee).isSuccess()) {
            employee.setNationality(Nationality.TURKISH);
            return new SuccessResult();
        } else {
            employee.setNationality(Nationality.OTHER);
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
