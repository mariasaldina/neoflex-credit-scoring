package com.creditscoring.statement.validation.maxage;

import com.creditscoring.statement.properties.PrescoringProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
public class MaxAgeValidator implements ConstraintValidator<ValidMaxAge, MaxAgeValidatable> {

    private final PrescoringProperties prescoringProperties;

    @Override
    public boolean isValid(MaxAgeValidatable obj, ConstraintValidatorContext context) {
        if (obj.term() == null || obj.birthdate() == null) {
            return false;
        }

        LocalDate payOffDate = LocalDate.now().plusMonths(obj.term());
        int ageAfterPayOff = Period.between(obj.birthdate(), payOffDate).getYears();

        boolean isValid = ageAfterPayOff <= prescoringProperties.age().max();
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            String violationMessage = String
                    .format("На момент выплаты кредита заёмщик будет старше %s лет",
                            prescoringProperties.age().max());
            context.buildConstraintViolationWithTemplate(violationMessage)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
