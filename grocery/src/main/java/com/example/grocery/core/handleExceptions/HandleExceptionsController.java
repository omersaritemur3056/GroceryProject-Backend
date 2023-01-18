package com.example.grocery.core.handleExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.exceptions.TokenRefreshException;
import com.example.grocery.core.utilities.results.ErrorResult;

@RestControllerAdvice
public class HandleExceptionsController {

    @ExceptionHandler(value = { BusinessException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResult handleBusinessException(BusinessException exception) {

        return new ErrorResult(exception.getLocalizedMessage());
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorResult handleTokenRefreshException(TokenRefreshException tokenRefreshException, WebRequest webRequest) {
        return new ErrorResult(tokenRefreshException.getMessage());
    }
}
