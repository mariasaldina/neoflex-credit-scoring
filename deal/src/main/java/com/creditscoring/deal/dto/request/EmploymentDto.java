package com.creditscoring.deal.dto.request;

import com.creditscoring.deal.enums.EmploymentPosition;
import com.creditscoring.deal.enums.EmploymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "Информация о трудоустройстве заёмщика")
public record EmploymentDto(

        @Schema(
                description = "Статус (трудоустроен, самозанятый, владелец бизнеса, безработный, студент, пенсионер)",
                example = "EMPLOYED"
        )
        @NotNull
        EmploymentStatus employmentStatus,

        @Schema(
                description = "ИНН работодателя",
                example = "0000000000"
        )
        @NotBlank
        @Pattern(regexp = "\\d{10}|\\d{12}")
        String employerINN,

        @Schema(
                description = "Заработная плата в рублях",
                example = "70000"
        )
        @NotNull
        @Positive
        BigDecimal salary,

        @Schema(
                description = "Должность (исполнитель, менеджер среднего / высшего звена, собственник)",
                example = "WORKER"
        )
        @NotNull
        EmploymentPosition position,

        @Schema(
                description = "Общий трудовой опыт в месяцах",
                example = "24"
        )
        @NotNull
        @Positive
        Integer workExperienceTotal,

        @Schema(
                description = "Трудовой опыт на текущем рабочем месте в месяцах",
                example = "10"
        )
        @NotNull
        @Positive
        Integer workExperienceCurrent
) {}
