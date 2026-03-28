package com.creditscoring.deal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Schema(description = "Заявка с данными для расчёта кредитных предложений")
public record LoanStatementRequestDto(

        @Schema(
                description = "Запрашиваемая сумма займа в рублях",
                example = "100000"
        )
        @NotNull
        @Positive
        BigDecimal amount,

        @Schema(
                description = "Срок займа в месяцах",
                example = "10"
        )
        @NotNull
        @Positive
        Integer term,

        @Schema(
                description = "Имя",
                example = "Иван"
        )
        @NotBlank
        String firstName,

        @Schema(
                description = "Фамилия",
                example = "Иванов"
        )
        @NotBlank
        String lastName,

        @Schema(
                description = "Отчество",
                example = "Иванович"
        )
        String middleName,

        @Schema(
                description = "Электронная почта",
                example = "example@mail.com"
        )
        @NotBlank
        String email,

        @Schema(
                description = "Дата рождения в формате ISO 8601",
                example = "2000-01-01"
        )
        @NotNull
        LocalDate birthdate,

        @Schema(
                description = "Серия паспорта",
                example = "0000"
        )
        @NotBlank
        String passportSeries,

        @Schema(
                description = "Номер паспорта",
                example = "000000"
        )
        @NotBlank
        String passportNumber
) {}
