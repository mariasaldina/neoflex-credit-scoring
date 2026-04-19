package com.creditscoring.statement.validation.minage;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinAgeValidator.class)
public @interface ValidMinAge {
    String message() default "Заёмщик младше минимального разрешённого возраста";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
