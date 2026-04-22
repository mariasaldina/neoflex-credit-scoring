package com.creditscoring.deal.exception;

import java.util.UUID;

public class NoSesCodeException extends RuntimeException {
    public NoSesCodeException(UUID statementId) {
        super("В заявке %s отсутствует код сессии".formatted(statementId));
    }
}
