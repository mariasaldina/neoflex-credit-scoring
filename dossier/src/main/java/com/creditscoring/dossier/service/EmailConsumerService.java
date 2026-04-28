package com.creditscoring.dossier.service;

import com.creditscoring.dossier.dto.EmailMessage;
import com.creditscoring.dossier.enums.EmailTheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailConsumerService {

    private final EmailService emailService;
    private final DealRestClient dealRestClient;

    @KafkaListener(topics = {
            "finish-registration",
            "create-documents",
            "send-documents",
            "send-ses",
            "credit-issued",
            "statement-denied"
    })
    public void consume(EmailMessage message) {
        log.debug("Считано сообщение для заявки {} со статусом {}", message.statementId(), message.theme());
        if (message.theme() == EmailTheme.SEND_DOCUMENTS) {
            // формирование документов
            dealRestClient.setStatusDocumentsCreated(message.statementId());
        }
        emailService.sendEmail(message);
    }
}