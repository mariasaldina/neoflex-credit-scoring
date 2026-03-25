package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.LoanOfferDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.entity.Client;
import com.creditscoring.deal.entity.Statement;
import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.ChangeType;
import com.creditscoring.deal.json.AppliedOffer;
import com.creditscoring.deal.json.StatusHistory;
import com.creditscoring.deal.repository.ClientRepository;
import com.creditscoring.deal.repository.StatementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;

    private final CalculatorClient calculatorClient;

    @Transactional
    public List<LoanOfferDto> saveStatement(LoanStatementRequestDto statementDto) {
        Client client = this.clientRepository.save(new Client(
                statementDto.firstName(),
                statementDto.lastName(),
                statementDto.middleName(),
                statementDto.birthdate(),
                statementDto.email()
        ));
        Statement statement = this.statementRepository.save(new Statement(client.getClientId()));

        return this.calculatorClient.getOffers(statementDto)
                .stream()
                .map(o -> new LoanOfferDto(
                        statement.getStatementId(),
                        o.requestedAmount(),
                        o.totalAmount(),
                        o.term(),
                        o.monthlyPayment(),
                        o.rate(),
                        o.isInsuranceEnabled(),
                        o.isSalaryClient()
                ))
                .toList();
    }

    public void selectOffer(LoanOfferDto appliedOffer) {


        Statement statement = this.statementRepository.findById(appliedOffer.statementId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный statementId")
        );
        statement.setStatus(ApplicationStatus.APPROVED);
        statement.setAppliedOffer(new AppliedOffer(
                appliedOffer.statementId(),
                appliedOffer.requestedAmount(),
                appliedOffer.totalAmount(),
                appliedOffer.term(),
                appliedOffer.monthlyPayment(),
                appliedOffer.rate(),
                appliedOffer.isInsuranceEnabled(),
                appliedOffer.isSalaryClient()
        ));
        statement.getStatusHistory().add(new StatusHistory(
                ApplicationStatus.APPROVED,
                ChangeType.AUTOMATIC
        ));
    }
}
