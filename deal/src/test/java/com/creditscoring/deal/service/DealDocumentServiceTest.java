package com.creditscoring.deal.service;

import com.creditscoring.deal.entity.Client;
import com.creditscoring.deal.entity.Credit;
import com.creditscoring.deal.entity.Statement;
import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.CreditStatus;
import com.creditscoring.deal.enums.EmailTheme;
import com.creditscoring.deal.exception.ApplicationStatusConflictException;
import com.creditscoring.deal.exception.InvalidSesCodeException;
import com.creditscoring.deal.mapper.CreditMapper;
import com.creditscoring.deal.repository.ClientRepository;
import com.creditscoring.deal.repository.StatementRepository;
import com.creditscoring.deal.utils.TestDataFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class DealDocumentServiceTest {

    @Autowired
    private DealDocumentService dealDocumentService;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private StatementRepository statementRepository;

    @MockitoBean
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private CreditMapper creditMapper;

    private Statement createStatementWithStatus(ApplicationStatus status) {
        Client client = clientRepository.save(TestDataFactory.createClient().build());
        Statement statement = statementRepository.save(new Statement(client));
        statement.setStatus(status);
        return statement;
    }

    @Test
    void sendDocuments_success() {
        Statement statement = createStatementWithStatus(ApplicationStatus.CC_APPROVED);
        dealDocumentService.sendDocuments(statement.getStatementId());

        assertEquals(ApplicationStatus.PREPARE_DOCUMENTS, statement.getStatus());
        verify(kafkaProducerService).send(
                statement.getClient().getEmail(),
                EmailTheme.SEND_DOCUMENTS,
                statement.getStatementId()
        );
    }

    @Test
    void sendDocuments_failure() {
        Statement statement = createStatementWithStatus(ApplicationStatus.CC_DENIED);

        assertThrows(
                ApplicationStatusConflictException.class,
                () -> dealDocumentService.sendDocuments(statement.getStatementId())
        );

        assertEquals(ApplicationStatus.CC_DENIED, statement.getStatus());
        verify(kafkaProducerService, never()).send(any(), any(), any());
    }

    @Test
    void signDocuments_success() {
        Statement statement = createStatementWithStatus(ApplicationStatus.DOCUMENTS_CREATED);
        dealDocumentService.signDocuments(statement.getStatementId());

        assertNotNull(statement.getSesCode());
        verify(kafkaProducerService).send(
                statement.getClient().getEmail(),
                EmailTheme.SEND_SES,
                statement.getStatementId(),
                statement.getSesCode()
        );
    }

    @Test
    void signDocuments_failure() {
        Statement statement = createStatementWithStatus(ApplicationStatus.CC_DENIED);

        assertThrows(
                ApplicationStatusConflictException.class,
                () -> dealDocumentService.signDocuments(statement.getStatementId())
        );

        assertEquals(ApplicationStatus.CC_DENIED, statement.getStatus());
        assertNull(statement.getSesCode());
        verify(kafkaProducerService, never()).send(any(), any(), any());
    }

    @Test
    void verifyCode_success() {
        Statement statement = createStatementWithStatus(ApplicationStatus.DOCUMENTS_CREATED);
        statement.setCredit(creditMapper.toCreditEntity(TestDataFactory.createCreditDto().build()));
        statement.setSesCode(UUID.randomUUID());

        dealDocumentService.verifyCode(statement.getStatementId(), statement.getSesCode());

        assertEquals(ApplicationStatus.CREDIT_ISSUED, statement.getStatus());
        assertEquals(CreditStatus.ISSUED, statement.getCredit().getCreditStatus());
        assertNotNull(statement.getSignDate());
        verify(kafkaProducerService).send(
                statement.getClient().getEmail(),
                EmailTheme.CREDIT_ISSUED,
                statement.getStatementId()
        );
    }

    @Test
    void verifyCode_failure() {
        Statement statement = createStatementWithStatus(ApplicationStatus.DOCUMENTS_CREATED);
        statement.setCredit(creditMapper.toCreditEntity(TestDataFactory.createCreditDto().build()));
        statement.setSesCode(UUID.randomUUID());

        assertThrows(
                InvalidSesCodeException.class,
                () -> dealDocumentService.verifyCode(statement.getStatementId(), UUID.randomUUID())
        );

        assertEquals(ApplicationStatus.DOCUMENTS_CREATED, statement.getStatus());
        assertEquals(CreditStatus.CALCULATED, statement.getCredit().getCreditStatus());
        verify(kafkaProducerService, never()).send(any(), any(), any());
    }
}
