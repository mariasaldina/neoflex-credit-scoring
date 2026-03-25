package com.creditscoring.deal.dto.request;

import com.creditscoring.deal.enums.Gender;
import com.creditscoring.deal.enums.MaritalStatus;

import java.time.LocalDate;

public record FinishRegistrationRequestDto(
        Gender gender,
        MaritalStatus maritalStatus,
        Integer dependentAmount,
        LocalDate passportIssueDate,
        String passportIssueBranch,
        EmploymentDto employment,
        String accountNumber
) {}