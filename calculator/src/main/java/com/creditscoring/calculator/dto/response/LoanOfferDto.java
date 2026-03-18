package com.creditscoring.calculator.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record LoanOfferDto(
        @Schema(
                description = "id кредитного предложения",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID statementId,

        @Schema(
                description = "Запрашиваемая сумма займа в рублях",
                example = "100000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal requestedAmount,

        @Schema(
                description = "Полная сумма кредита в рублях",
                example = "105000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal totalAmount,

        @Schema(
                description = "Срок займа в месяцах",
                example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer term,

        @Schema(
                description = "Ежемесячный платёж в рублях",
                example = "10000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal monthlyPayment,

        @Schema(
                description = "Годовая ставка в %",
                example = "5",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal rate,

        @Schema(
                description = "Страховка включена",
                example = "false",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Boolean isInsuranceEnabled,

        @Schema(
                description = "Клиент является зарплатным",
                example = "false",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Boolean isSalaryClient
) {}