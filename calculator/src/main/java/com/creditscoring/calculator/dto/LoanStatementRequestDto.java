package com.creditscoring.calculator.dto;

import com.creditscoring.calculator.validation.ValidAge;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

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

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Past
        @ValidAge
        LocalDate birthdate,

        @NotBlank
        @Pattern(regexp = "\\d{4}")
        String passportSeries,

        @NotBlank
        @Pattern(regexp = "\\d{6}")
        String passportNumber
) {}
