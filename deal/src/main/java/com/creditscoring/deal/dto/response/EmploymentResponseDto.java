package com.creditscoring.deal.dto.response;

import com.creditscoring.deal.enums.EmploymentPosition;
import com.creditscoring.deal.enums.EmploymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record EmploymentResponseDto(
        UUID employmentId,
        EmploymentStatus employmentStatus,
        String employerINN,
        BigDecimal salary,
        EmploymentPosition position,
        Integer workExperienceTotal,
        Integer workExperienceCurrent
) {}