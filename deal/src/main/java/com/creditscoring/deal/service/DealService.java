package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.calculator.response.CreditDto;
import com.creditscoring.deal.dto.request.LoanOfferDto;
import com.creditscoring.deal.dto.calculator.request.ScoringDataDto;
import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.dto.request.StatusDto;
import com.creditscoring.deal.entity.*;
import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.EmailTheme;
import com.creditscoring.deal.exception.ApplicationStatusConflictException;
import com.creditscoring.deal.exception.StatementNotFoundException;
import com.creditscoring.deal.mapper.*;
import com.creditscoring.deal.repository.ClientRepository;
import com.creditscoring.deal.repository.StatementRepository;
import com.creditscoring.deal.service.hook.LockHook;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;

    private final ClientMapper clientMapper;
    private final PassportMapper passportMapper;
    private final OfferMapper offerMapper;
    private final CreditMapper creditMapper;
    private final ScoringDataMapper scoringDataMapper;

    private final CalculatorRestClient calculatorRestClient;
    private final KafkaProducerService producer;

    private final LockHook lockHook;

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
    public List<LoanOfferDto> saveStatement(LoanStatementRequestDto statementDto) {
        Client client = this.clientRepository.save(clientMapper.toClientEntity(statementDto));
        Statement statement = this.statementRepository.save(new Statement(client));

        log.debug("Клиент {} сохранен в БД", client.getClientId());
        log.debug("Заявка {} сохранена в БД", statement.getStatementId());

        return this.calculatorRestClient.getOffers(statementDto)
                .stream()
                .map(o -> offerMapper.updateStatementId(o, statement.getStatementId()))
                .toList();
    }

    @Transactional
    public void selectOffer(LoanOfferDto appliedOffer) {
        Statement statement = this.getStatement(
                appliedOffer.statementId(),
                ApplicationStatus.PREAPPROVAL
        );
        log.debug("Обновляется заявка {}", statement.getStatementId());

        lockHook.afterLockCaptured();

        statement.applyOffer(offerMapper.toAppliedOfferJson(appliedOffer));

        log.debug("Статус заявки {} изменён на APPROVED", statement.getStatementId());
        log.debug("Заявка {} сохранена с предложением: isInsuranceEnabled={}, isSalaryClient={}",
                statement.getStatementId(),
                appliedOffer.isInsuranceEnabled(),
                appliedOffer.isSalaryClient());

        producer.send(
                statement.getClient().getEmail(),
                EmailTheme.FINISH_REGISTRATION,
                statement.getStatementId()
        );
    }

    @Transactional(noRollbackFor = ResponseStatusException.class)
    public void calculateCredit(FinishRegistrationRequestDto finishDto, UUID statementId) {
        Statement statement = this.getStatement(statementId, ApplicationStatus.APPROVED);
        ScoringDataDto scoringDataDto = scoringDataMapper.toScoringDataDto(statement, finishDto);

        clientMapper.updateClientEntity(finishDto, statement.getClient());
        passportMapper.updatePassportEntity(finishDto, statement.getClient().getPassport());
        log.debug("Данные клиента {} обновлены", statement.getClient().getClientId());

        try {
            CreditDto creditDto = this.calculatorRestClient.getCredit(scoringDataDto);
            statement.saveCredit(creditMapper.toCreditEntity(creditDto));

            log.debug("Кредитное предложение {} сохранено в БД", statement.getCredit().getCreditId());
            log.debug("Статус заявки {} изменён на CC_APPROVED", statement.getStatementId());

            producer.send(
                    statement.getClient().getEmail(),
                    EmailTheme.CREATE_DOCUMENTS,
                    statementId
            );
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                statement.changeStatus(ApplicationStatus.CC_DENIED);

                log.debug("Статус заявки {} изменён на CC_DENIED", statement.getStatementId());

                producer.send(
                        statement.getClient().getEmail(),
                        EmailTheme.STATEMENT_DENIED,
                        statementId
                );
            }
            throw e;
        }
    }

    @Transactional
    public void changeStatus(UUID statementId, StatusDto dto) {
        Statement statement = this.statementRepository.findById(statementId).orElseThrow(
                () -> new StatementNotFoundException(statementId)
        );
        statement.changeStatus(dto.status(), dto.changeType());
    }
}
