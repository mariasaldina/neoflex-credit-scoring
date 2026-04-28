package com.creditscoring.dossier.enums;

import lombok.Getter;

@Getter
public enum EmailTheme {
    FINISH_REGISTRATION("Заявка предварительно одобрена"),
    CREATE_DOCUMENTS("Документы для оформления кредита"),
    SEND_DOCUMENTS("Подписание кредитных документов"),
    SEND_SES("Код подтверждения"),
    CREDIT_ISSUED("Кредит оформлен"),
    STATEMENT_DENIED("Отказ по заявке");

    private final String subject;

    EmailTheme(String subject) {
        this.subject = subject;
    }
}