package com.creditscoring.calculator;

import com.creditscoring.calculator.service.AnnuityModelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

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
}
