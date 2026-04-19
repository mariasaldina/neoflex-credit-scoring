package com.creditscoring.statement.validation.amount;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AmountValidator.class)
public @interface ValidAmount {
    String message() default "Сумма займа меньше минимальной или больше максимальной разрешённой суммы";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
