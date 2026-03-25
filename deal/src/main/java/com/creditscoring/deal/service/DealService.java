package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.LoanOfferDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.entity.Client;
import com.creditscoring.deal.entity.Statement;
import com.creditscoring.deal.repository.ClientRepository;
import com.creditscoring.deal.repository.StatementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;

    private final CalculatorClient calculatorClient;

    @Transactional
    public List<LoanOfferDto> saveStatement(LoanStatementRequestDto dto) {
        Client client = this.clientRepository.save(new Client(
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.birthdate(),
                dto.email()
        ));
        Statement statement = this.statementRepository.save(new Statement(client.getClientId()));

        return this.calculatorClient.getOffers(dto)
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
}
