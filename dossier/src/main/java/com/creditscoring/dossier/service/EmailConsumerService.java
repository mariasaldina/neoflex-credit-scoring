package com.creditscoring.dossier.service;

import com.creditscoring.dossier.dto.EmailMessage;
import com.creditscoring.dossier.dto.StatusDto;
import com.creditscoring.dossier.enums.ApplicationStatus;
import com.creditscoring.dossier.enums.EmailTheme;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class EmailConsumerService {

    private final EmailService emailService;
    private final RestClient restClient;

    @KafkaListener(topics = {
            "finish-registration",
            "create-documents",
            "send-documents",
            "send-ses",
            "credit-issued",
            "statement-denied"
    })
    public void consume(EmailMessage message) {
        if (message.theme() == EmailTheme.SEND_DOCUMENTS) {
            // формирование документов
            restClient
                    .put()
                    .uri("/admin/statement/%s/status".formatted(message.statementId()))
                    .body(new StatusDto(ApplicationStatus.DOCUMENTS_CREATED))
                    .retrieve()
                    .toBodilessEntity();
        }

        emailService.sendEmail(message);
    }
}