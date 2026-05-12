package com.creditscoring.deal.dto.response;

import com.creditscoring.deal.enums.Gender;
import com.creditscoring.deal.enums.MaritalStatus;

import java.time.LocalDate;
import java.util.UUID;

public record ClientDto(
        UUID clientId,
        String lastName,
        String firstName,
        String middleName,
        LocalDate birthdate,
        String email,
        Gender gender,
        MaritalStatus maritalStatus,
        Integer dependentAmount,
        String accountNumber,
        PassportDto passport,
        EmploymentResponseDto employment
) { }
