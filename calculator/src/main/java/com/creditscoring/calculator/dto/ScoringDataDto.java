package com.creditscoring.calculator.dto;

import com.creditscoring.calculator.enums.Gender;
import com.creditscoring.calculator.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ScoringDataDto(
        BigDecimal amount,
        Integer term,
        String firstName,
        String lastName,
        String middleName,
        Gender gender,
        LocalDate birthdate,
        String passportSeries,
        String passportNumber,
        LocalDate passportIssueDate,
        String passportIssueBranch,
        MaritalStatus maritalStatus,
        Integer dependentAmount,
        EmploymentDto employment,
        String accountNumber,
        Boolean isInsuranceEnabled,
        Boolean isSalaryClient
) {}