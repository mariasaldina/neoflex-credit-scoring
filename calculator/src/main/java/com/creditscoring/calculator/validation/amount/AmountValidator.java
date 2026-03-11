package com.creditscoring.calculator.validation.amount;

import com.creditscoring.calculator.configuration.PrescoringProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class AmountValidator implements ConstraintValidator<ValidAmount, BigDecimal> {

    private final PrescoringProperties prescoringProperties;

    public AmountValidator(
            PrescoringProperties prescoringProperties
    ) {
        this.prescoringProperties = prescoringProperties;
    }

    @Override
    public boolean isValid(BigDecimal amount, ConstraintValidatorContext context) {
        if (amount == null) return false;

        BigDecimal min = prescoringProperties.amount().min();
        BigDecimal max = prescoringProperties.amount().max();

        return amount.compareTo(min) >= 0 && amount.compareTo(max) <= 0;
    }
}
