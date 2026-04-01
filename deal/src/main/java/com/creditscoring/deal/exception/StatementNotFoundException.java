package com.creditscoring.deal.exception;

import java.util.UUID;

public class StatementNotFoundException extends RuntimeException {
    public StatementNotFoundException(UUID statementId) {
        super(String.format("Заявка с id %s не найдена", statementId));
    }
}
