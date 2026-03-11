package com.creditscoring.calculator.dto;

import com.creditscoring.calculator.enums.Gender;
import com.creditscoring.calculator.enums.MaritalStatus;
import com.creditscoring.calculator.validation.minage.ValidMinAge;
import com.creditscoring.calculator.validation.amount.ValidAmount;
import com.creditscoring.calculator.validation.term.ValidTerm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ScoringDataDto(
        @NotNull
        @Positive
        @ValidAmount
        BigDecimal amount,

        @NotNull
        @ValidTerm
        Integer term,

        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        String middleName,

        @NotNull
        Gender gender,

        @NotNull
        @ValidMinAge
        LocalDate birthdate,

        @NotBlank
        @Pattern(regexp = "\\d{4}")
        String passportSeries,

        @NotBlank
        @Pattern(regexp = "\\d{6}")
        String passportNumber,

        @Past
        LocalDate passportIssueDate,
        @NotBlank
        String passportIssueBranch,

        @NotNull
        MaritalStatus maritalStatus,

        @NotNull
        @PositiveOrZero
        Integer dependentAmount,

        @Valid
        @NotNull
        EmploymentDto employment,

        @NotBlank
        @Pattern(regexp = "[A-Z]{2}\\d{2}[A-Z0-9]{11,30}")
        String accountNumber,

        @NotNull
        Boolean isInsuranceEnabled,
        @NotNull
        Boolean isSalaryClient
) {}