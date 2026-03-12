package com.creditscoring.calculator.service;

import com.creditscoring.calculator.configuration.LoanProperties;
import com.creditscoring.calculator.domain.FullPaymentData;
import com.creditscoring.calculator.dto.CreditDto;
import com.creditscoring.calculator.dto.LoanOfferDto;
import com.creditscoring.calculator.dto.PaymentScheduleElementDto;
import com.creditscoring.calculator.dto.ScoringDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class CalculatorService {
    private final LoanProperties loanProperties;
    private final ScoringService scoringService;
    private final AnnuityModelService annuityModelService;
    private final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    public CalculatorService(
            LoanProperties loanProperties,
            ScoringService scoringService,
            AnnuityModelService annuityModelService
    ) {
        this.loanProperties = loanProperties;
        this.scoringService = scoringService;
        this.annuityModelService = annuityModelService;
    }

    private BigDecimal getInsuranceRate(BigDecimal rate) {
        return rate.add(loanProperties.insurance().rate());
    }

    private BigDecimal getSalaryClientRate(BigDecimal rate) {
        return rate.add(loanProperties.salaryClient().rate());
    }

    private BigDecimal getInsuranceAmount(BigDecimal amount) {
        BigDecimal insuranceAmount = amount
                .multiply(loanProperties
                        .insurance()
                        .pricePercent()
                        .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                        .add(BigDecimal.ONE)
                )
                .setScale(2, RoundingMode.HALF_UP);
        logger.debug("\nСумма кредита вместе со страховкой: {}\n", insuranceAmount);
        return insuranceAmount;
    }

    private LoanOfferDto formLoanOffer (
            BigDecimal amount,
            BigDecimal rate,
            Integer term,
            Boolean isInsuranceEnabled,
            Boolean isSalaryClient
    ) {
        BigDecimal monthlyPayment = annuityModelService.monthlyPayment(amount, rate, term);
        BigDecimal totalAmount = annuityModelService
                .paymentSchedule(term, rate, amount, monthlyPayment, LocalDate.now())
                .psk();

        return new LoanOfferDto(
                UUID.randomUUID(),
                amount,
                totalAmount,
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
        BigDecimal insuranceAmount = getInsuranceAmount(amount);

        return Stream.of(
                this.formLoanOffer(amount, baseRate, term, false, false),
                this.formLoanOffer(
                        insuranceAmount,
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
                        insuranceAmount,
                        getInsuranceRate(getSalaryClientRate(baseRate)),
                        term,
                        true,
                        true
                )
        )
                .sorted(Comparator.comparing(LoanOfferDto::rate).reversed())
                .toList();
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

        BigDecimal monthlyPayment = annuityModelService.monthlyPayment(
                amount,
                rate,
                scoringData.term()
        );

        FullPaymentData paymentData = annuityModelService.paymentSchedule(
                scoringData.term(),
                rate,
                amount,
                monthlyPayment,
                LocalDate.now()
        );

        return new CreditDto(
                amount,
                scoringData.term(),
                monthlyPayment,
                rate,
                paymentData.psk(),
                scoringData.isInsuranceEnabled(),
                scoringData.isSalaryClient(),
                paymentData.paymentSchedule()
        );
    }
}
