package com.creditscoring.statement.validation.term;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TermValidator.class)
public @interface ValidTerm {
    String message() default "Срок займа меньше минимального или больше максимального";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
