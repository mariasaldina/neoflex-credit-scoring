package com.creditscoring.calculator.validation.maxage;

import java.time.LocalDate;

public interface MaxAgeValidatable {
    Integer term();
    LocalDate birthdate();
}
