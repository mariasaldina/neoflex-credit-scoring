package com.creditscoring.statement.validation.maxage;

import java.time.LocalDate;

public interface MaxAgeValidatable {
    Integer term();
    LocalDate birthdate();
}
