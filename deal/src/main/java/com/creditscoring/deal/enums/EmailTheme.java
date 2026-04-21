package com.creditscoring.deal.enums;

import lombok.Getter;

@Getter
public enum EmailTheme {
    FINISH_REGISTRATION("finish-registration"),
    CREATE_DOCUMENTS("create-documents"),
    SEND_DOCUMENTS("send-documents"),
    SEND_SES("send-ses"),
    CREDIT_ISSUED("credit-issued"),
    STATEMENT_DENIED("statement-denied");

    private final String topic;

    EmailTheme(String topic) {
        this.topic = topic;
    }
}