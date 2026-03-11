package com.creditscoring.calculator.service;

import com.creditscoring.calculator.configuration.LoanProperties;
import com.creditscoring.calculator.dto.CreditDto;
import com.creditscoring.calculator.dto.LoanOfferDto;
import com.creditscoring.calculator.dto.PaymentScheduleElementDto;
import com.creditscoring.calculator.dto.ScoringDataDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class CalculatorService {
    private final LoanProperties loanProperties;
    private final ScoringService scoringService;

    public CalculatorService(
            LoanProperties loanProperties,
            ScoringService scoringService
    ) {
        this.loanProperties = loanProperties;
        this.scoringService = scoringService;
    }

    private BigDecimal getInsuranceRate(BigDecimal rate) {
        return rate.add(loanProperties.insurance().rate());
    }

    private BigDecimal getSalaryClientRate(BigDecimal rate) {
        return rate.add(loanProperties.salaryClient().rate());
    }

    private BigDecimal getInsuranceAmount(BigDecimal amount) {
        return amount
                .multiply(loanProperties
                        .insurance()
                        .pricePercent()
                        .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                        .add(BigDecimal.ONE)
                )
                .setScale(2, RoundingMode.HALF_UP);
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
        BigDecimal baseRate = loanProperties.baseRate();

        return Stream.of(
                this.formLoanOffer(amount, baseRate, term, false, false),
                this.formLoanOffer(
                        getInsuranceAmount(amount),
                        getInsuranceRate(baseRate),
                        term,
                        true,
                        false
                ),
                this.formLoanOffer(
                        amount,
                        getSalaryClientRate(baseRate),
                        term,
                        false,
                        true
                ),
                this.formLoanOffer(
                        getInsuranceAmount(amount),
                        getInsuranceRate(getSalaryClientRate(baseRate)),
                        term,
                        true,
                        true
                )
        )
                .sorted(Comparator.comparing(LoanOfferDto::rate).reversed())
                .toList();
    }

    private List<PaymentScheduleElementDto> annuityPaymentSchedule(
            Integer term,
            BigDecimal rate,
            BigDecimal monthlyPayment,
            LocalDate start
    ) {
        List<PaymentScheduleElementDto> payments = new ArrayList<>();

        LocalDate date = start;
        BigDecimal debt = monthlyPayment.multiply(new BigDecimal(term));
        BigDecimal monthlyRate = rate.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);
        for (int i = 0; i < term; ++i) {
            BigDecimal interestPayment = debt.multiply(monthlyRate);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            debt = debt.subtract(monthlyPayment);
            date = date.plusMonths(1);

            payments.add(new PaymentScheduleElementDto(
                    i + 1,
                    date,
                    monthlyPayment,
                    interestPayment,
                    debtPayment,
                    debt
            ));
        }

        return payments;
    }

    public CreditDto calculateCredit(ScoringDataDto scoringData) {
        BigDecimal rate = this.scoringService.scoring(loanProperties.baseRate(), scoringData);

        BigDecimal amount;
        if (scoringData.isInsuranceEnabled()) {
            amount = getInsuranceAmount(scoringData.amount());
        } else {
            amount = scoringData.amount();
        }

        if (scoringData.isSalaryClient()) {
            rate = getSalaryClientRate(rate);
        }
        if (scoringData.isInsuranceEnabled()) {
            rate = getInsuranceRate(rate);
        }

        BigDecimal monthlyPayment = this.annuityMonthlyPayment(
                amount,
                rate,
                scoringData.term()
        );

        List<PaymentScheduleElementDto> schedule = annuityPaymentSchedule(
                scoringData.term(),
                rate,
                monthlyPayment,
                LocalDate.now()
        );

        return new CreditDto(
                amount,
                scoringData.term(),
                monthlyPayment,
                rate,
                monthlyPayment.multiply(new BigDecimal(scoringData.term())),
                scoringData.isInsuranceEnabled(),
                scoringData.isSalaryClient(),
                schedule
        );
    }
}
