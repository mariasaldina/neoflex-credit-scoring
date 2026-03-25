package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.CreditDto;
import com.creditscoring.deal.dto.LoanOfferDto;
import com.creditscoring.deal.dto.ScoringDataDto;
import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.entity.*;
import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.ChangeType;
import com.creditscoring.deal.json.AppliedOffer;
import com.creditscoring.deal.json.StatusHistory;
import com.creditscoring.deal.mapper.*;
import com.creditscoring.deal.repository.ClientRepository;
import com.creditscoring.deal.repository.CreditRepository;
import com.creditscoring.deal.repository.StatementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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

    @Transactional
    public List<LoanOfferDto> saveStatement(LoanStatementRequestDto statementDto) {
        Client client = this.clientRepository.save(clientMapper.toEntity(statementDto));
        client.setPassport(passportMapper.toEntity(statementDto));
        Statement statement = this.statementRepository.save(new Statement(client));

        return this.calculatorClient.getOffers(statementDto)
                .stream()
                .map(o -> offerMapper.updateStatementId(o, statement.getStatementId()))
                .toList();
    }

    @Transactional
    public void selectOffer(LoanOfferDto appliedOffer) {
        Statement statement = this.statementRepository.findById(appliedOffer.statementId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный statementId")
        );
        statement.setStatus(ApplicationStatus.APPROVED);
        statement.setAppliedOffer(offerMapper.toEntityField(appliedOffer));
        statement.getStatusHistory().add(new StatusHistory(
                ApplicationStatus.APPROVED,
                ChangeType.AUTOMATIC
        ));
    }

    @Transactional
    public void calculateCredit(FinishRegistrationRequestDto finishDto, UUID statementId) {
        Statement statement = this.statementRepository.findById(statementId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный statementId")
        );
        ScoringDataDto scoringDataDto = scoringDataMapper.toDto(statement, finishDto);

        CreditDto creditDto;
        try {
            creditDto = this.calculatorClient.getCredit(scoringDataDto);
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                statement.setStatus(ApplicationStatus.CC_DENIED);
            }
            throw e;
        }

        this.creditRepository.save(creditMapper.toEntity(creditDto));
        clientMapper.updateEntity(finishDto, statement.getClient());
        passportMapper.updateEntity(finishDto, statement.getClient().getPassport());

        statement.setStatus(ApplicationStatus.CC_APPROVED);
        statement.getStatusHistory().add(new StatusHistory(
                ApplicationStatus.CC_APPROVED,
                ChangeType.AUTOMATIC
        ));
    }
}
