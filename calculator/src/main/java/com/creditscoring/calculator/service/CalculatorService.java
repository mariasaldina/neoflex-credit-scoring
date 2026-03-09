package com.creditscoring.calculator.service;

import com.creditscoring.calculator.configuration.LoanProperties;
import com.creditscoring.calculator.dto.LoanOfferDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class CalculatorService {
    private final LoanProperties loanProperties;

    public CalculatorService(
            LoanProperties loanProperties
    ) {
        this.loanProperties = loanProperties;
    }

    private BigDecimal annuityMonthlyPayment(
            BigDecimal amount,
            BigDecimal rate,
            Integer term
    ) {
        BigDecimal monthlyRate = rate.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);
        BigDecimal rateMultiplier = monthlyRate.add(BigDecimal.ONE).pow(term);

        return amount
                .multiply(monthlyRate.multiply(rateMultiplier)
                        .divide(rateMultiplier.subtract(BigDecimal.ONE), 10, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private LoanOfferDto formLoanOffer (
            BigDecimal amount,
            BigDecimal rate,
            Integer term,
            Boolean isInsuranceEnabled,
            Boolean isSalaryClent
    ) {
        BigDecimal monthlyPayment = this.annuityMonthlyPayment(amount, rate, term);

        return new LoanOfferDto(
                UUID.randomUUID(),
                amount,
                monthlyPayment.multiply(new BigDecimal(term)),
                term,
                monthlyPayment,
                rate,
                isInsuranceEnabled,
                isSalaryClent
        );
    }

    public List<LoanOfferDto> createOffers(
            BigDecimal amount,
            Integer term
    ) {
        return List.of(
                this.formLoanOffer(amount, loanProperties.getBaseRate(), term, false, false),
                this.formLoanOffer(
                        amount,
                        loanProperties.getBaseRate().subtract(loanProperties.getInsurance().getRateReduction()),
                        term,
                        true,
                        false
                )
        );
    }
}
