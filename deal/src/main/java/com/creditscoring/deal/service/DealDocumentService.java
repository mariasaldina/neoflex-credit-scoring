package com.creditscoring.deal.service;

import com.creditscoring.deal.entity.Statement;
import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.CreditStatus;
import com.creditscoring.deal.enums.EmailTheme;
import com.creditscoring.deal.exception.ApplicationStatusConflictException;
import com.creditscoring.deal.exception.InvalidSesCodeException;
import com.creditscoring.deal.exception.NoSesCodeException;
import com.creditscoring.deal.exception.StatementNotFoundException;
import com.creditscoring.deal.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealDocumentService {

    private final StatementRepository statementRepository;
    private final KafkaProducerService producer;

    private Statement getStatement(UUID statementId, ApplicationStatus expectedStatus) {
        Statement statement = this.statementRepository.findByStatementId(statementId)
                .orElseThrow(() -> new StatementNotFoundException(statementId));

        if (statement.getStatus() != expectedStatus) {
            throw new ApplicationStatusConflictException(
                    statement.getStatus(),
                    expectedStatus
            );
        }

        return statement;
    }

    @Transactional
    public void sendDocuments(UUID statementId) {
        Statement statement = this.getStatement(statementId, ApplicationStatus.CC_APPROVED);
        statement.changeStatus(ApplicationStatus.PREPARE_DOCUMENTS);
        log.debug("Статус заявки {} изменен на PREPARE_DOCUMENTS", statementId);

        producer.send(
                statement.getClient().getEmail(),
                EmailTheme.SEND_DOCUMENTS,
                statementId
        );
    }

    @Transactional
    public void signDocuments(UUID statementId) {
        Statement statement = this.getStatement(statementId, ApplicationStatus.DOCUMENTS_CREATED);

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
        Statement statement = this.getStatement(statementId, ApplicationStatus.DOCUMENTS_CREATED);
        if (statement.getSesCode() == null) {
            throw new NoSesCodeException(statementId);
        }

        if (!statement.getSesCode().equals(sesCode)) {
            log.debug("Ошибка подписания заявки {}: Код сессии отсутствует или не валиден", statementId);
            throw new InvalidSesCodeException(statementId);
        }

        statement.changeStatus(ApplicationStatus.DOCUMENTS_SIGNED);
        statement.sign();

        log.debug("Заявка {} подписана, кредит {} одобрен", statementId, statement.getCredit().getCreditId());

        producer.send(
                statement.getClient().getEmail(),
                EmailTheme.CREDIT_ISSUED,
                statementId
        );
    }
}
