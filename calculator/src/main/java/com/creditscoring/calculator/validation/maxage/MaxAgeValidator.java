package com.creditscoring.calculator.validation.maxage;

import com.creditscoring.calculator.configuration.PrescoringProperties;
import com.creditscoring.calculator.domain.MaxAgeValidatable;
import com.creditscoring.calculator.dto.LoanStatementRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MaxAgeValidator implements ConstraintValidator<ValidMaxAge, MaxAgeValidatable> {

    private final PrescoringProperties prescoringProperties;

    public MaxAgeValidator(
            PrescoringProperties prescoringProperties
    ) {
        this.prescoringProperties = prescoringProperties;
    }

    @Override
    public boolean isValid(MaxAgeValidatable obj, ConstraintValidatorContext context) {
        if (obj.term() == null || obj.birthdate() == null) {
            return false;
        }

        LocalDate payOffDate = LocalDate.now().plusMonths(obj.term());
        int ageAfterPayOff = Period.between(obj.birthdate(), payOffDate).getYears();
        return ageAfterPayOff <= prescoringProperties.age().max();
    }
}
