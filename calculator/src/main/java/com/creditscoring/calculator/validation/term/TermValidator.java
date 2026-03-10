package com.creditscoring.calculator.validation.term;

import com.creditscoring.calculator.configuration.LoanProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TermValidator implements ConstraintValidator<ValidTerm, Integer> {

    private final LoanProperties loanProperties;

    public TermValidator(
            LoanProperties loanProperties
    ) {
        this.loanProperties = loanProperties;
    }

    @Override
    public boolean isValid(Integer term, ConstraintValidatorContext context) {
        if (term == null) return false;
        return term >= loanProperties.getTerm().getMin() && term <= loanProperties.getTerm().getMax();
    }
}
