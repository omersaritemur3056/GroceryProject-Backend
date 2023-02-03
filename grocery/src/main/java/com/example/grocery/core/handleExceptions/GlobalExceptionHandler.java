package com.example.grocery.core.handleExceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.exceptions.TokenRefreshException;
import com.example.grocery.core.utilities.results.ErrorDataResult;
import com.example.grocery.core.utilities.results.ErrorResult;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleValidationException(MethodArgumentNotValidException exceptions) {
        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError fieldError : exceptions.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ErrorDataResult<>(validationErrors, "Validation errors!");
    }
}
