package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.calculator.response.CreditDto;
import com.creditscoring.deal.dto.request.LoanOfferDto;
import com.creditscoring.deal.dto.calculator.request.ScoringDataDto;
import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.entity.*;
import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.ChangeType;
import com.creditscoring.deal.json.StatusHistory;
import com.creditscoring.deal.mapper.*;
import com.creditscoring.deal.repository.ClientRepository;
import com.creditscoring.deal.repository.CreditRepository;
import com.creditscoring.deal.repository.StatementRepository;
import jakarta.transaction.Transactional;
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
    private final CreditRepository creditRepository;

    private final ClientMapper clientMapper;
    private final PassportMapper passportMapper;
    private final OfferMapper offerMapper;
    private final CreditMapper creditMapper;
    private final ScoringDataMapper scoringDataMapper;

    private final CalculatorClient calculatorClient;
    private final StatementStatusService statementStatusService;

    @Transactional
    public List<LoanOfferDto> saveStatement(LoanStatementRequestDto statementDto) {
        Client client = this.clientRepository.save(clientMapper.toEntity(statementDto));
        client.setPassport(passportMapper.toEntity(statementDto));
        Statement statement = this.statementRepository.save(new Statement(client));

        log.debug("Клиент {} сохранен в БД", client.getClientId());
        log.debug("Заявка {} сохранена в БД", statement.getStatementId());

        return this.calculatorClient.getOffers(statementDto)
                .stream()
                .map(o -> offerMapper.updateStatementId(o, statement.getStatementId()))
                .toList();
    }

    @Transactional
    public void selectOffer(LoanOfferDto appliedOffer) {
        Statement statement = this.statementRepository.findById(appliedOffer.statementId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Заявка не найдена")
        );
        if (!List.of(ApplicationStatus.PREAPPROVAL, ApplicationStatus.APPROVED).contains(statement.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Статус заявки не позволяет изменить кредитное предложение"
            );
        }

        statement.changeStatus(ApplicationStatus.APPROVED);
        statement.setAppliedOffer(offerMapper.toEntityField(appliedOffer));

        log.debug("Статус заявки {} изменён на APPROVED", statement.getStatementId());
        log.debug("Заявка {} сохранена с предложением: isInsuranceEnabled={}, isSalaryClient={}",
                statement.getStatementId(),
                appliedOffer.isInsuranceEnabled(),
                appliedOffer.isSalaryClient());
    }

    @Transactional
    public void calculateCredit(FinishRegistrationRequestDto finishDto, UUID statementId) {
        Statement statement = this.statementRepository.findById(statementId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Заявка не найдена")
        );
        if (statement.getAppliedOffer() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Не выбрано кредитное предложение");
        }

        ScoringDataDto scoringDataDto = scoringDataMapper.toDto(statement, finishDto);

        clientMapper.updateEntity(finishDto, statement.getClient());
        passportMapper.updateEntity(finishDto, statement.getClient().getPassport());
        log.debug("Данные клиента {} обновлены", statement.getClient().getClientId());

        CreditDto creditDto;
        try {
            creditDto = this.calculatorClient.getCredit(scoringDataDto);
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                statementStatusService.deny(statementId);
                log.debug("Статус заявки {} изменён на CC_DENIED", statement.getStatementId());
            }
            throw e;
        }

        Credit credit = this.creditRepository.save(creditMapper.toEntity(creditDto));
        log.debug("Кредитное предложение {} сохранено в БД", credit.getCreditId());

        statement.changeStatus(ApplicationStatus.CC_APPROVED);
        log.debug("Статус заявки {} изменён на CC_APPROVED", statement.getStatementId());
    }
}
