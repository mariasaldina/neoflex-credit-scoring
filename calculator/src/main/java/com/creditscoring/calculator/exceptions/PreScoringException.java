package com.creditscoring.calculator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PreScoringException extends RuntimeException {
    public PreScoringException(String message) {
        super(message);
    }
}
