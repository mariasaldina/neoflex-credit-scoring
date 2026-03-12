package com.creditscoring.calculator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ScoringException extends ResponseStatusException {
    public ScoringException(String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    @Override
    public String getMessage() {
        return "Ошибка скоринга"; // всегда одно сообщение
    }
}
