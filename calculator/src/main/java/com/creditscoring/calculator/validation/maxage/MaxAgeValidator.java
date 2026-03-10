package com.creditscoring.calculator.validation.maxage;

import com.creditscoring.calculator.configuration.LoanProperties;
import com.creditscoring.calculator.dto.LoanStatementRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MaxAgeValidator implements ConstraintValidator<ValidMaxAge, LoanStatementRequestDto> {

    private final LoanProperties loanProperties;

    public MaxAgeValidator(
            LoanProperties loanProperties
    ) {
        this.loanProperties = loanProperties;
    }

    @Override
    public boolean isValid(LoanStatementRequestDto statement, ConstraintValidatorContext context) {
        if (statement.term() == null || statement.birthdate() == null) {
            return false;
        }

        LocalDate payOffDate = LocalDate.now().plusMonths(statement.term());
        int ageAfterPayOff = Period.between(statement.birthdate(), payOffDate).getYears();
        return ageAfterPayOff <= loanProperties.getAge().getMax();
    }
}
