package com.creditscoring.deal.exception;

import java.util.UUID;

public class InvalidSesCodeException extends RuntimeException {
    public InvalidSesCodeException(UUID statementId) {
        super(String.format("Заявка с id %s не прошла проверку ses_code", statementId));
    }
}
