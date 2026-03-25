package com.creditscoring.deal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanStatementRequestDto(

        @Schema(
                description = "Запрашиваемая сумма займа в рублях",
                example = "100000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal amount,

        @Schema(
                description = "Срок займа в месяцах",
                example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer term,

        @Schema(
                description = "Имя",
                example = "Иван",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String firstName,

        @Schema(
                description = "Фамилия",
                example = "Иванов",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String lastName,

        @Schema(
                description = "Отчество",
                example = "Иванович",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String middleName,

        @Schema(
                description = "Электронная почта",
                example = "example@mail.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,

        @Schema(
                description = "Дата рождения в формате ISO 8601",
                example = "2000-01-01",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDate birthdate,

        @Schema(
                description = "Серия паспорта",
                example = "0000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String passportSeries,

        @Schema(
                description = "Номер паспорта",
                example = "000000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String passportNumber
) {}
