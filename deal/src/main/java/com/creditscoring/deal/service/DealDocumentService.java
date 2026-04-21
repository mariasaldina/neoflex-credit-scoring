package com.creditscoring.deal.service;

import com.creditscoring.deal.entity.Statement;
import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.EmailTheme;
import com.creditscoring.deal.exception.InvalidSesCodeException;
import com.creditscoring.deal.exception.StatementNotFoundException;
import com.creditscoring.deal.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealDocumentService {

    private final StatementRepository statementRepository;
    private final KafkaProducerService producer;

    @Transactional
    public void sendDocuments(UUID statementId) {
        Statement statement = this.statementRepository.findByStatementId(statementId)
                .orElseThrow(() -> new StatementNotFoundException(statementId));
        statement.changeStatus(ApplicationStatus.PREPARE_DOCUMENTS);

        producer.send(
                statement.getClient().getEmail(),
                EmailTheme.SEND_DOCUMENTS,
                statementId
        );
    }

    @Transactional
    public void signDocuments(UUID statementId) {
        Statement statement = this.statementRepository.findByStatementId(statementId)
                .orElseThrow(() -> new StatementNotFoundException(statementId));
        UUID sesCode = UUID.randomUUID();
        statement.setSesCode(sesCode);

        producer.send(
                statement.getClient().getEmail(),
                EmailTheme.SEND_SES,
                statementId,
                sesCode
        );
    }

    @Transactional
    public void verifyCode(UUID statementId, UUID sesCode) {
        Statement statement = this.statementRepository.findByStatementId(statementId)
                .orElseThrow(() -> new StatementNotFoundException(statementId));

        if (statement.getSesCode() != sesCode) {
            throw new InvalidSesCodeException(statementId);
        }

        statement.setStatus(ApplicationStatus.DOCUMENTS_SIGNED);
        statement.setStatus(ApplicationStatus.CREDIT_ISSUED);

        producer.send(
                statement.getClient().getEmail(),
                EmailTheme.CREDIT_ISSUED,
                statementId
        );
    }
}
