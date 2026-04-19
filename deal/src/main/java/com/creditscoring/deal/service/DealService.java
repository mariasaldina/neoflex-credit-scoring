package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.calculator.response.CreditDto;
import com.creditscoring.deal.dto.request.LoanOfferDto;
import com.creditscoring.deal.dto.calculator.request.ScoringDataDto;
import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.entity.*;
import com.creditscoring.deal.enums.ApplicationStatus;
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

    private final LockHook lockHook;

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
        Statement statement = this.statementRepository.findByStatementId(
                appliedOffer.statementId()
        ).orElseThrow(
                () -> new StatementNotFoundException(appliedOffer.statementId())
        );

        log.debug("Обновляется заявка {}", statement.getStatementId());

        lockHook.afterLockCaptured();

        if (statement.getStatus() != ApplicationStatus.PREAPPROVAL) {
            throw new ApplicationStatusConflictException(statement.getStatus(), ApplicationStatus.PREAPPROVAL);
        }

        statement.applyOffer(offerMapper.toAppliedOfferJson(appliedOffer));

        log.debug("Статус заявки {} изменён на APPROVED", statement.getStatementId());
        log.debug("Заявка {} сохранена с предложением: isInsuranceEnabled={}, isSalaryClient={}",
                statement.getStatementId(),
                appliedOffer.isInsuranceEnabled(),
                appliedOffer.isSalaryClient());
    }

    @Transactional(noRollbackFor = ResponseStatusException.class)
    public void calculateCredit(FinishRegistrationRequestDto finishDto, UUID statementId) {
        Statement statement = this.statementRepository.findById(statementId).orElseThrow(
                () -> new StatementNotFoundException(statementId)
        );
        if (statement.getStatus() != ApplicationStatus.APPROVED) {
            throw new ApplicationStatusConflictException(statement.getStatus(), ApplicationStatus.APPROVED);
        }

        ScoringDataDto scoringDataDto = scoringDataMapper.toScoringDataDto(statement, finishDto);

        clientMapper.updateClientEntity(finishDto, statement.getClient());
        passportMapper.updatePassportEntity(finishDto, statement.getClient().getPassport());
        log.debug("Данные клиента {} обновлены", statement.getClient().getClientId());

        CreditDto creditDto;
        try {
            creditDto = this.calculatorRestClient.getCredit(scoringDataDto);
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                statement.changeStatus(ApplicationStatus.CC_DENIED);
                log.debug("Статус заявки {} изменён на CC_DENIED", statement.getStatementId());
            }
            throw e;
        }

        statement.saveCredit(creditMapper.toCreditEntity(creditDto));
        log.debug("Кредитное предложение {} сохранено в БД", statement.getCredit().getCreditId());
        log.debug("Статус заявки {} изменён на CC_APPROVED", statement.getStatementId());
    }
}
