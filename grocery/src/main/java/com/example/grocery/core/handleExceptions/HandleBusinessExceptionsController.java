package com.example.grocery.core.handleExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.ErrorResult;

//@ControllerAdvice
@RestControllerAdvice
public class HandleBusinessExceptionsController {

    @ExceptionHandler(value = { BusinessException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResult handleBusinessException(BusinessException exception) {

        ErrorResult error = new ErrorResult(exception.getLocalizedMessage());

        return error;
    }
    // metodu ister ErrorResult ile yap ister ResponseEntity<> ile...
    // ResponseEntity ile yaz ödev olarak
}
