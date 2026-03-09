package com.creditscoring.calculator.validation.maxage;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxAgeValidator.class)
public @interface ValidMaxAge {
    String message() default "Borrower must not be older than 75 by the time they pay off the loan";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
