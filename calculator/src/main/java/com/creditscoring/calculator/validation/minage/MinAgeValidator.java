package com.creditscoring.calculator.validation.minage;

import com.creditscoring.calculator.configuration.LoanProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MinAgeValidator implements ConstraintValidator<ValidMinAge, LocalDate> {

    private final LoanProperties loanProperties;

    public MinAgeValidator(
            LoanProperties loanProperties
    ) {
        this.loanProperties = loanProperties;
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) return false;

        int age = Period.between(birthdate, LocalDate.now()).getYears();
        return age >= loanProperties.getAge().getMin();
    }
}
