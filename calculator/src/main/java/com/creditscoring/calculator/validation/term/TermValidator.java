package com.creditscoring.calculator.validation.term;

import com.creditscoring.calculator.configuration.PrescoringProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TermValidator implements ConstraintValidator<ValidTerm, Integer> {

    private final PrescoringProperties prescoringProperties;

    public TermValidator(
            PrescoringProperties prescoringProperties
    ) {
        this.prescoringProperties = prescoringProperties;
    }

    @Override
    public boolean isValid(Integer term, ConstraintValidatorContext context) {
        if (term == null) return false;
        return term >= prescoringProperties.term().min() && term <= prescoringProperties.term().max();
    }
}
