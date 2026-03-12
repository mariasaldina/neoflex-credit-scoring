package com.creditscoring.calculator;

import com.creditscoring.calculator.configuration.LoanProperties;
import com.creditscoring.calculator.domain.FullPaymentData;
import com.creditscoring.calculator.dto.CreditDto;
import com.creditscoring.calculator.dto.EmploymentDto;
import com.creditscoring.calculator.dto.LoanOfferDto;
import com.creditscoring.calculator.dto.ScoringDataDto;
import com.creditscoring.calculator.enums.EmploymentStatus;
import com.creditscoring.calculator.enums.Gender;
import com.creditscoring.calculator.enums.MaritalStatus;
import com.creditscoring.calculator.enums.Position;
import com.creditscoring.calculator.service.AnnuityModelService;
import com.creditscoring.calculator.service.CalculatorService;
import com.creditscoring.calculator.service.ScoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
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

    @InjectMocks
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

                () -> assertEquals(new BigDecimal("20"), offers.get(0).rate()),
                () -> assertEquals(new BigDecimal("19"), offers.get(1).rate()),
                () -> assertEquals(new BigDecimal("17"), offers.get(2).rate()),
                () -> assertEquals(new BigDecimal("16"), offers.get(3).rate()),

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
        BigDecimal amount = new BigDecimal("100000");
        Integer term = 10;

        CreditDto credit = calculatorService.calculateCredit(new ScoringDataDto(
                amount,
                term,
                "Иван",
                "Иванов",
                "Иванович",
                Gender.MALE,
                LocalDate.of(2000, Month.MARCH, 9),
                "0000",
                "000000",
                LocalDate.of(2020, Month.APRIL, 12),
                "ГУ МВД России по г. Москва",
                MaritalStatus.MARRIED,
                0,
                new EmploymentDto(
                        EmploymentStatus.EMPLOYED,
                        "0000000000",
                        new BigDecimal("70000"),
                        Position.MIDDLE_MANAGER,
                        40,
                        10
                ),
                "RU000000000000000",
                true,
                false
        ));

        assertAll(
                () -> assertEquals(amount, credit.amount()),
                () -> assertEquals(new BigDecimal("12"), credit.rate())
        );
    }
}
