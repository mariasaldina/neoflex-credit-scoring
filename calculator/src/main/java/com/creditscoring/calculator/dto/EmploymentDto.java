package com.creditscoring.calculator.dto;

import com.creditscoring.calculator.enums.EmploymentStatus;
import com.creditscoring.calculator.enums.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record EmploymentDto(
        @NotNull
        EmploymentStatus employmentStatus,

        @NotBlank
        @Pattern(regexp = "\\d{10}|\\d{12}")
        String employerINN,

        @NotNull
        @Positive
        BigDecimal salary,

        @NotNull
        Position position,

        @NotNull
        @Positive
        Integer workExperienceTotal,

        @NotNull
        @Positive
        Integer workExperienceCurrent
) {}
