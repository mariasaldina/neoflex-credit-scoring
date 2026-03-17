package com.creditscoring.calculator.validation.minage;

import com.creditscoring.calculator.properties.PrescoringProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
public class MinAgeValidator implements ConstraintValidator<ValidMinAge, LocalDate> {

    private final PrescoringProperties prescoringProperties;

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) return false;

        int age = Period.between(birthdate, LocalDate.now()).getYears();
        boolean isValid = age >= prescoringProperties.age().min();

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            String violationMessage = String.format("Заёмщик младше %s лет", prescoringProperties.age().min());
            context.buildConstraintViolationWithTemplate(violationMessage)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
