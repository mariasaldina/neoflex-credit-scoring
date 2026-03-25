package com.creditscoring.deal.dto.request;

import com.creditscoring.deal.enums.EmploymentPosition;
import com.creditscoring.deal.enums.EmploymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Информация о трудоустройстве заёмщика")
public record EmploymentDto(

        @Schema(
                description = "Статус (трудоустроен, самозанятый, владелец бизнеса, безработный, студент, пенсионер)",
                example = "EMPLOYED",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        EmploymentStatus employmentStatus,

        @Schema(
                description = "ИНН работодателя",
                example = "0000000000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String employerINN,

        @Schema(
                description = "Заработная плата в рублях",
                example = "70000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal salary,

        @Schema(
                description = "Должность (исполнитель, менеджер среднего / высшего звена, собственник)",
                example = "WORKER",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        EmploymentPosition position,

        @Schema(
                description = "Общий трудовой опыт в месяцах",
                example = "24",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer workExperienceTotal,

        @Schema(
                description = "Трудовой опыт на текущем рабочем месте в месяцах",
                example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer workExperienceCurrent
) {}
