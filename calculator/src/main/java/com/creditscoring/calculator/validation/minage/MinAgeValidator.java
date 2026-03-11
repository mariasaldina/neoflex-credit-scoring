package com.creditscoring.calculator.validation.minage;

import com.creditscoring.calculator.configuration.PrescoringProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MinAgeValidator implements ConstraintValidator<ValidMinAge, LocalDate> {

    private final PrescoringProperties prescoringProperties;

    public MinAgeValidator(
            PrescoringProperties prescoringProperties
    ) {
        this.prescoringProperties = prescoringProperties;
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) return false;

        int age = Period.between(birthdate, LocalDate.now()).getYears();
        return age >= prescoringProperties.age().min();
    }
}
