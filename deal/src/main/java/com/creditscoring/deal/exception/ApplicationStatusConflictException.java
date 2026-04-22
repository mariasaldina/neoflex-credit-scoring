package com.creditscoring.deal.exception;

import com.creditscoring.deal.enums.ApplicationStatus;

public class ApplicationStatusConflictException extends RuntimeException {
    public ApplicationStatusConflictException(ApplicationStatus actual, ApplicationStatus expected) {
        super(String.format("Текущий статус заявки: %s, ожидаемый: %s", actual, expected));
    }

    public ApplicationStatusConflictException() {
        super("Некорректный статус заявки");
    }
}
