package com.creditscoring.calculator.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public record CreditDto(
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
                description = "Ежемесячный платёж в рублях",
                example = "10000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal monthlyPayment,

        @Schema(
                description = "Итоговая годовая ставка в %",
                example = "5",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal rate,

        @Schema(
                description = "Полная сумма кредита в рублях",
                example = "105000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal psk,

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
        Boolean isSalaryClient,

        @Schema(
                description = "График ежемесячных платежей",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        List<PaymentScheduleElementDto> paymentSchedule
) {}