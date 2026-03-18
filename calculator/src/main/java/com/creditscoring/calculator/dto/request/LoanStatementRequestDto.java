package com.creditscoring.calculator.dto.request;

import com.creditscoring.calculator.validation.maxage.MaxAgeValidatable;
import com.creditscoring.calculator.validation.minage.ValidMinAge;
import com.creditscoring.calculator.validation.amount.ValidAmount;
import com.creditscoring.calculator.validation.maxage.ValidMaxAge;
import com.creditscoring.calculator.validation.term.ValidTerm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@ValidMaxAge
public record LoanStatementRequestDto(

        @Schema(
                description = "Запрашиваемая сумма займа в рублях",
                example = "100000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        @Positive
        @ValidAmount
        BigDecimal amount,

        @Schema(
                description = "Срок займа в месяцах",
                example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        @Positive
        @ValidTerm
        Integer term,

        @Schema(
                description = "Имя",
                example = "Иван",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Size(min = 1, max = 30)
        String firstName,

        @Schema(
                description = "Фамилия",
                example = "Иванов",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Size(min = 1, max = 30)
        String lastName,

        @Schema(
                description = "Отчество",
                example = "Иванович",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Size(min = 1, max = 30)
        String middleName,

        @Schema(
                description = "Электронная почта",
                example = "example@mail.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Pattern(regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$")
        String email,

        @Schema(
                description = "Дата рождения в формате ISO 8601",
                example = "2000-01-01",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        @Past
        @ValidMinAge
        LocalDate birthdate,

        @Schema(
                description = "Серия паспорта",
                example = "0000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Pattern(regexp = "\\d{4}")
        String passportSeries,

        @Schema(
                description = "Номер паспорта",
                example = "000000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Pattern(regexp = "\\d{6}")
        String passportNumber
) implements MaxAgeValidatable {}
