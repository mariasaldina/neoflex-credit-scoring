package com.creditscoring.calculator.dto;

import com.creditscoring.calculator.validation.adult.IsAdult;
import com.creditscoring.calculator.validation.maxage.ValidMaxAge;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@ValidMaxAge
public record LoanStatementRequestDto(
        @NotNull
        @Positive
        @DecimalMin(value = "10000")
        @DecimalMax(value = "10000000")
        BigDecimal amount,

        @NotNull
        @Positive
        @Min(value = 3)
        @Max(value = 60)
        Integer term,

        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        String middleName,

        @NotBlank
        @Email
        String email,

        @Past
        @IsAdult
        LocalDate birthdate,

        @NotBlank
        @Pattern(regexp = "\\d{4}")
        String passportSeries,

        @NotBlank
        @Pattern(regexp = "\\d{6}")
        String passportNumber
) {}
