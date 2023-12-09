package com.example.airplanems.exceptions;

import com.example.airplanems.model.enums.ExceptionsEnum;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final ExceptionsEnum exceptionsEnum;

    public GeneralException(ExceptionsEnum exceptions) {
        super(exceptions.toString());
        this.exceptionsEnum = exceptions;
    }
}
