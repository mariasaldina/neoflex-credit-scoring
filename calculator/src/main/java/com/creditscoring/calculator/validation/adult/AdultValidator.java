package com.creditscoring.calculator.validation.adult;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AdultValidator implements ConstraintValidator<IsAdult, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        return age >= 18;
    }
}
