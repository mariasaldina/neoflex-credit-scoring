package com.creditscoring.dossier.utils;

import com.creditscoring.dossier.dto.EmailMessage;
import com.creditscoring.dossier.enums.EmailTheme;

import java.util.UUID;

public class TestDataFactory {

    public static EmailMessage.EmailMessageBuilder createEmailMessage() {
        return EmailMessage.builder()
                .address("test@mail.com")
                .theme(EmailTheme.CREATE_DOCUMENTS)
                .statementId(UUID.randomUUID())
                .text("some text");
    }
}
