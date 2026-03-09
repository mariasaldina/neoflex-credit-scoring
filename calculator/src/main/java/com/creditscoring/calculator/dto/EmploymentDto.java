package com.creditscoring.calculator.dto;

import com.creditscoring.calculator.enums.EmploymentStatus;
import com.creditscoring.calculator.enums.Position;

import java.math.BigDecimal;

public record EmploymentDto(
        EmploymentStatus employmentStatus,
        String employerINN,
        BigDecimal salary,
        Position position,
        Integer workExperienceTotal,
        Integer workExperienceCurrent
) {}
