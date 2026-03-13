package com.creditscoring.calculator.domain;

import java.time.LocalDate;

public interface MaxAgeValidatable {
    public Integer term();
    public LocalDate birthdate();
}
