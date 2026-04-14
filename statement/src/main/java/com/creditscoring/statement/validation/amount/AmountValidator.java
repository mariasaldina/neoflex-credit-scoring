package com.creditscoring.statement.validation.amount;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class AmountValidator implements ConstraintValidator<ValidAmount, BigDecimal> {

    private final com.creditscoring.statement.properties.PrescoringProperties prescoringProperties;

    @Override
    public boolean isValid(BigDecimal amount, ConstraintValidatorContext context) {
        if (amount == null) return false;

        BigDecimal min = prescoringProperties.amount().min();
        BigDecimal max = prescoringProperties.amount().max();

        boolean isLess = amount.compareTo(min) < 0;
        boolean isMore = amount.compareTo(max) > 0;

        if (isLess || isMore) {
            context.disableDefaultConstraintViolation();
            String violationMessage =
                    isLess
                            ? String.format("Сумма займа меньше %s рублей", min)
                            : String.format("Сумма займа больше %s рублей", max);
            context.buildConstraintViolationWithTemplate(violationMessage)
                    .addConstraintViolation();
        }

        return !isLess && !isMore;
    }
}
