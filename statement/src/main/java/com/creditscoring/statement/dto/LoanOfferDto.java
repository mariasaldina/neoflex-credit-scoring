package com.creditscoring.statement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Schema(description = "Предварительное кредитное предложение")
public record LoanOfferDto(

        @Schema(
                description = "id кредитного предложения",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @NotNull
        UUID statementId,

        @Schema(
                description = "Запрашиваемая сумма займа в рублях",
                example = "100000"
        )
        @NotNull
        @Positive
        BigDecimal requestedAmount,

        @Schema(
                description = "Полная сумма кредита в рублях",
                example = "105000"
        )
        @NotNull
        @Positive
        BigDecimal totalAmount,

        @Schema(
                description = "Срок займа в месяцах",
                example = "10"
        )
        @NotNull
        @Positive
        Integer term,

        @Schema(
                description = "Ежемесячный платёж в рублях",
                example = "10000"
        )
        @NotNull
        @Positive
        BigDecimal monthlyPayment,

        @Schema(
                description = "Годовая ставка в %",
                example = "5"
        )
        @NotNull
        @Positive
        BigDecimal rate,

        @Schema(
                description = "Страховка включена",
                example = "false"
        )
        @NotNull
        Boolean isInsuranceEnabled,

        @Schema(
                description = "Клиент является зарплатным",
                example = "false"
        )
        @NotNull
        Boolean isSalaryClient
) {}