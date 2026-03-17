package com.creditscoring.calculator.validation.term;

import com.creditscoring.calculator.properties.PrescoringProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TermValidator implements ConstraintValidator<ValidTerm, Integer> {

    private final PrescoringProperties prescoringProperties;

    @Override
    public boolean isValid(Integer term, ConstraintValidatorContext context) {
        if (term == null) return false;

        Integer min = prescoringProperties.term().min();
        Integer max = prescoringProperties.term().max();

        if (term < min || term > max) {
            context.disableDefaultConstraintViolation();
            String violationMessage =
                    term < min
                            ? String.format("Срок займа меньше %s месяцев", min)
                            : String.format("Срок займа больше %s месяцев", max);
            context.buildConstraintViolationWithTemplate(violationMessage)
                    .addConstraintViolation();
        }

        return term >= min && term <= max;
    }
}
