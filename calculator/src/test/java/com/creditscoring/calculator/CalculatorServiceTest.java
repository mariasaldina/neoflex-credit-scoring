package com.creditscoring.calculator;

import com.creditscoring.calculator.configuration.LoanProperties;
import com.creditscoring.calculator.domain.FullPaymentData;
import com.creditscoring.calculator.dto.CreditDto;
import com.creditscoring.calculator.dto.LoanOfferDto;
import com.creditscoring.calculator.service.AnnuityModelService;
import com.creditscoring.calculator.service.CalculatorService;
import com.creditscoring.calculator.service.ScoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {

    @Mock
    private ScoringService scoringService;

    @Mock
    private AnnuityModelService annuityModelService;

    private CalculatorService calculatorService;

    @BeforeEach
    void setup() {
        LoanProperties loanProperties = new LoanProperties(
                new BigDecimal("20"),
                new LoanProperties.Insurance(
                        new BigDecimal("5"),
                        new BigDecimal("-3")
                ),
                new LoanProperties.SalaryClient(
                        new BigDecimal("-1")
                )
        );

        calculatorService = new CalculatorService(
                loanProperties,
                scoringService,
                annuityModelService
        );
    }

    private void assertOfferExists(
            List<LoanOfferDto> offers,
            Boolean isInsuranceEnabled,
            Boolean isSalaryClient,
            String expectedRate
    ) {
        LoanOfferDto offer = offers.stream()
                .filter(o ->
                        o.isInsuranceEnabled() == isInsuranceEnabled && o.isSalaryClient() == isSalaryClient)
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        String.format(
                            "Предложение с isInsuranceEnabled = %b и isSalaryClient = %b не найдено",
                            isInsuranceEnabled,
                            isSalaryClient
                        )
                ));

        assertThat(offer.rate()).isEqualByComparingTo(expectedRate);
    }

    @Test
    void testCreateOffers() {
        BigDecimal amount = new BigDecimal("100000");
        Integer term = 10;

        when(annuityModelService.monthlyPayment(any(), any(), any()))
                .thenReturn(new BigDecimal("10000"));
        when(annuityModelService.paymentSchedule(any(), any(), any(), any(), any()))
                .thenReturn(new FullPaymentData(
                        List.of(),
                        BigDecimal.ZERO
                ));

        List<LoanOfferDto> offers = calculatorService.createOffers(amount, term);

        assertThat(offers).hasSize(4);
        assertOfferExists(offers, false, false, "20");
        assertOfferExists(offers, false, true, "19");
        assertOfferExists(offers, true, false, "17");
        assertOfferExists(offers, true, true, "16");
    }

    @Test
    void testCalculateCredit() {
        when(scoringService.scoring(any(), any()))
                .thenReturn(new BigDecimal("20"));

        when(annuityModelService.monthlyPayment(any(), any(), any()))
                .thenReturn(new BigDecimal("10000"));
        when(annuityModelService.paymentSchedule(any(), any(), any(), any(), any()))
                .thenReturn(new FullPaymentData(
                        List.of(),
                        BigDecimal.ZERO
                ));

        BigDecimal amount = new BigDecimal("100000");

        CreditDto credit = calculatorService.calculateCredit(
                TestDataFactory.createScoringDataBuilder()
                        .amount(amount)
                        .isInsuranceEnabled(true)
                        .build()
        );

        assertAll(
                () -> assertThat(credit.amount()).isEqualByComparingTo("105000"),
                () -> assertThat(credit.rate()).isEqualByComparingTo("17")
        );
    }
}
