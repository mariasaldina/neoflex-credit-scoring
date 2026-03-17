package com.creditscoring.calculator.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentScheduleElementDto(
        @Schema(
                description = "Порядковый номер платежа",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer number,

        @Schema(
                description = "Дата платежа в формате ISO 8601",
                example = "2025-02-03",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDate date,

        @Schema(
                description = "Общая сумма платежа в рублях",
                example = "10000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal totalPayment,

        @Schema(
                description = "Сумма, погашающая проценты, в рублях",
                example = "2000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal interestPayment,

        @Schema(
                description = "Сумма, погашающая часть тела кредита, в рублях",
                example = "8000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal debtPayment,

        @Schema(
                description = "Остаток долга после платежа, в рублях",
                example = "50000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal remainingDebt
) {}