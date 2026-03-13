package com.creditscoring.calculator.dto;

import com.creditscoring.calculator.domain.MaxAgeValidatable;
import com.creditscoring.calculator.validation.minage.ValidMinAge;
import com.creditscoring.calculator.validation.amount.ValidAmount;
import com.creditscoring.calculator.validation.maxage.ValidMaxAge;
import com.creditscoring.calculator.validation.term.ValidTerm;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@ValidMaxAge
public record LoanStatementRequestDto(
        @NotNull
        @Positive
        @ValidAmount
        BigDecimal amount,

        @NotNull
        @Positive
        @ValidTerm
        Integer term,

        @NotBlank
        @Size(min = 2, max = 30)
        String firstName,
        @NotBlank
        @Size(min = 2, max = 30)
        String lastName,
        @Size(min = 2, max = 30)
        String middleName,

        @NotBlank
        @Pattern(regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$")
        String email,

        @NotNull
        @Past
        @ValidMinAge
        LocalDate birthdate,

        @NotBlank
        @Pattern(regexp = "\\d{4}")
        String passportSeries,

        @NotBlank
        @Pattern(regexp = "\\d{6}")
        String passportNumber
) implements MaxAgeValidatable {}
