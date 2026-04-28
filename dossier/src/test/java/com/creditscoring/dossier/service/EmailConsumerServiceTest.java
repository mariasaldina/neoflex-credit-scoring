package com.creditscoring.dossier.service;

import com.creditscoring.dossier.dto.EmailMessage;
import com.creditscoring.dossier.enums.EmailTheme;
import com.creditscoring.dossier.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailConsumerServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private DealRestClient dealRestClient;

    @InjectMocks
    private EmailConsumerService emailConsumerService;

    @Test
    void consume_success() {
        EmailMessage message = TestDataFactory.createEmailMessage().build();
        emailConsumerService.consume(message);

        verify(dealRestClient, never()).setStatusDocumentsCreated(any());
        verify(emailService).sendEmail(message);
    }

    @Test
    void consumeWithSendDocuments_success() {
        EmailMessage message = TestDataFactory.createEmailMessage()
                .theme(EmailTheme.SEND_DOCUMENTS)
                .build();
        emailConsumerService.consume(message);

        verify(dealRestClient).setStatusDocumentsCreated(message.statementId());
        verify(emailService).sendEmail(message);
    }
}
