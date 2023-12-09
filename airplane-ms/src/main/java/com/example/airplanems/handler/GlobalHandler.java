package com.example.airplanems.handler;

import com.example.airplanems.exceptions.GeneralException;
import com.example.airplanems.model.dto.response.ExceptionResponse;
import com.example.airplanems.model.enums.ExceptionsEnum;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.constraints.NotNull;
import org.springframework.validation.BindException;

@RestControllerAdvice
public class GlobalHandler extends DefaultErrorAttributes {

    @ExceptionHandler(GeneralException.class)
    public ExceptionResponse handleException(GeneralException generalException) {
        ExceptionsEnum exceptions = generalException.getExceptionsEnum();

        return ExceptionResponse.of(exceptions.getMessage(), exceptions.getHttpStatus());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handlerValidationException(@NotNull BindException exception) {
        FieldError fieldError = exception.getFieldError();

        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Validation error occurred!";

        return ExceptionResponse.of(errorMessage,HttpStatus.BAD_REQUEST);
    }
}
