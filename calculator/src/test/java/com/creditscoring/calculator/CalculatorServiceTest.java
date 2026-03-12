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

        assertAll(
                () -> assertEquals(4, offers.size()),

                () -> assertTrue(new BigDecimal("20").compareTo(offers.get(0).rate()) == 0),
                () -> assertTrue(new BigDecimal("19").compareTo(offers.get(1).rate()) == 0),
                () -> assertTrue(new BigDecimal("17").compareTo(offers.get(2).rate()) == 0),
                () -> assertTrue(new BigDecimal("16").compareTo(offers.get(3).rate()) == 0),

                () -> assertFalse(offers.get(0).isInsuranceEnabled()),
                () -> assertFalse(offers.get(0).isSalaryClient()),

                () -> assertFalse(offers.get(1).isInsuranceEnabled()),
                () -> assertTrue(offers.get(1).isSalaryClient()),

                () -> assertTrue(offers.get(2).isInsuranceEnabled()),
                () -> assertFalse(offers.get(2).isSalaryClient()),

                () -> assertTrue(offers.get(3).isInsuranceEnabled()),
                () -> assertTrue(offers.get(3).isSalaryClient())
        );
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
                () -> assertTrue(new BigDecimal("105000").compareTo(credit.amount()) == 0),
                () -> assertTrue(new BigDecimal("17").compareTo(credit.rate()) == 0)
        );
    }
}
