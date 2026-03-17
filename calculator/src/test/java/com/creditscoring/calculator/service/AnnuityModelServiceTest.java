package com.creditscoring.calculator.service;

import com.creditscoring.calculator.domain.FullPaymentData;
import com.creditscoring.calculator.dto.response.PaymentScheduleElementDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnnuityModelServiceTest {

    private final AnnuityModelService annuityModelService = new AnnuityModelService();

    @ParameterizedTest
    @CsvSource({
            "100000, 20, 10"
    })
    void monthlyPaymentTest(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthlyPayment = annuityModelService.monthlyPayment(amount, rate, term);
        assertThat(monthlyPayment).isEqualByComparingTo(new BigDecimal("10939.38"));
    }

    @ParameterizedTest
    @CsvSource({
            "10, 20, 100000, 2026-03-13, 109456.58, 11002.16"
    })
    void paymentScheduleTest(
            Integer term,
            BigDecimal rate,
            BigDecimal amount,
            LocalDate issueDate,
            BigDecimal expectedPsk,
            BigDecimal expectedLastPayment
    ) {
        FullPaymentData paymentData = annuityModelService.paymentSchedule(
                term,
                rate,
                amount,
                annuityModelService.monthlyPayment(
                        amount,
                        rate,
                        term
                ),
                issueDate
        );
        List<PaymentScheduleElementDto> schedule = paymentData.paymentSchedule();
        BigDecimal psk = paymentData.psk();

        BigDecimal totalDebtPayment = schedule.stream()
                        .map(PaymentScheduleElementDto::debtPayment)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalInterestPayment = schedule.stream()
                .map(PaymentScheduleElementDto::interestPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertAll(
                () -> assertThat(schedule).hasSize(term),
                () -> assertThat(totalDebtPayment).isEqualByComparingTo(amount),
                () -> assertThat(totalInterestPayment).isEqualByComparingTo(psk.subtract(amount)),
                () -> assertThat(schedule.getLast().remainingDebt()).isEqualByComparingTo(BigDecimal.ZERO),

                () -> assertThat(psk).isEqualByComparingTo(expectedPsk),
                () -> assertThat(schedule.getLast().totalPayment()).isEqualByComparingTo(expectedLastPayment)
        );
    }
}
