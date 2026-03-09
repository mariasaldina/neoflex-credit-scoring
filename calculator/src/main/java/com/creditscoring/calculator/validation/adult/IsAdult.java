package com.creditscoring.calculator.validation.adult;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdultValidator.class)
public @interface IsAdult {
    String message() default "Borrower must be older than 18";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
