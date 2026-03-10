package com.creditscoring.calculator.validation.amount;

import com.creditscoring.calculator.configuration.LoanProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class AmountValidator implements ConstraintValidator<ValidAmount, BigDecimal> {

    private final LoanProperties loanProperties;

    public AmountValidator(
            LoanProperties loanProperties
    ) {
        this.loanProperties = loanProperties;
    }

    @Override
    public boolean isValid(BigDecimal amount, ConstraintValidatorContext context) {
        if (amount == null) return false;

        BigDecimal min = loanProperties.getAmount().getMin();
        BigDecimal max = loanProperties.getAmount().getMax();

        return amount.compareTo(min) >= 0 && amount.compareTo(max) <= 0;
    }
}
