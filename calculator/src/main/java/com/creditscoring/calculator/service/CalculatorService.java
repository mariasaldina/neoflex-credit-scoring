package com.creditscoring.calculator.service;

import com.creditscoring.calculator.configuration.LoanProperties;
import com.creditscoring.calculator.dto.CreditDto;
import com.creditscoring.calculator.dto.LoanOfferDto;
import com.creditscoring.calculator.dto.ScoringDataDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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
            Boolean isSalaryClient
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
                isSalaryClient
        );
    }

    public List<LoanOfferDto> createOffers(
            BigDecimal amount,
            Integer term
    ) {
        BigDecimal baseRate = loanProperties.getBaseRate();
        BigDecimal insuranceRateReduction = loanProperties.getInsurance().getRateReduction();
        BigDecimal salaryClientRateReduction = loanProperties.getSalaryClient().getRateReduction();
        BigDecimal insurancePrice = amount
                .multiply(loanProperties
                        .getInsurance()
                        .getPricePercent()
                        .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                        .add(BigDecimal.ONE)
                )
                .setScale(2, RoundingMode.HALF_UP);

        return Stream.of(
                this.formLoanOffer(amount, baseRate, term, false, false),
                this.formLoanOffer(
                        insurancePrice,
                        baseRate.subtract(insuranceRateReduction),
                        term,
                        true,
                        false
                ),
                this.formLoanOffer(
                        amount,
                        baseRate.subtract(salaryClientRateReduction),
                        term,
                        false,
                        true
                ),
                this.formLoanOffer(
                        insurancePrice,
                        baseRate.subtract(insuranceRateReduction).subtract(salaryClientRateReduction),
                        term,
                        true,
                        true
                )
        )
                .sorted(Comparator.comparing(LoanOfferDto::rate).reversed())
                .toList();
    }

//    public CreditDto scoring(ScoringDataDto scoringData) {
//
//    }
}
