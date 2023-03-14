package com.example.grocery.core.security.services;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.core.mailing.service.EmailService;
import com.example.grocery.core.security.models.User;
import com.example.grocery.core.security.repository.UserRepository;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserBusinessRules {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    public Result isUsernameExist(String username) {
        if (userRepository.existsByUsername(username)) {
            log.warn(Messages.LogMessages.LogWarnMessages.USERNAME_EXIST, username);
            throw new BusinessException(Messages.ErrorMessages.USERNAME_EXIST);
        }
        return new SuccessResult();
    }

    public Result isEmailExist(String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn(Messages.LogMessages.LogWarnMessages.USER_EMAIL_REPEATED, email);
            throw new BusinessException(Messages.ErrorMessages.EMAIL_REPEATED);
        }
        return new SuccessResult();
    }

    public Result isValidPassword(String password, String username) {
        if (password.contains(username)) {
            log.warn(Messages.LogMessages.LogWarnMessages.USER_PASSWORD_NOT_VALID, password, username);
            throw new BusinessException(Messages.ErrorMessages.PASSWORD_NOT_VALID);
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
        User checkField = new User();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(Messages.ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }

    public void isVerifiedEmail(String email) {
        log.warn(Messages.LogMessages.LogWarnMessages.EMAIL_NOT_VERIFIED, email);
        if (!emailService.sendActivationEmail(email).isSuccess()) {
            throw new BusinessException(Messages.ErrorMessages.EMAIL_NOT_VERIFIED);
        }
    }
}
