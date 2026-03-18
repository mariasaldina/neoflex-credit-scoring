package com.creditscoring.calculator.dto.request;

import com.creditscoring.calculator.domain.MaxAgeValidatable;
import com.creditscoring.calculator.enums.Gender;
import com.creditscoring.calculator.enums.MaritalStatus;
import com.creditscoring.calculator.validation.maxage.ValidMaxAge;
import com.creditscoring.calculator.validation.minage.ValidMinAge;
import com.creditscoring.calculator.validation.amount.ValidAmount;
import com.creditscoring.calculator.validation.term.ValidTerm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@ValidMaxAge
@Schema(description = "Данные для скоринга и расчёта полной стоимости кредита")
public record ScoringDataDto(

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
                description = "Пол",
                example = "MALE",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        Gender gender,

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
        String passportNumber,

        @Schema(
                description = "Дата выдачи паспорта в формате ISO 8601",
                example = "2020-01-01",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Past
        LocalDate passportIssueDate,

        @Schema(
                description = "Орган выдачи паспорта",
                example = "ГУ МВД России по г. Москва",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String passportIssueBranch,

        @Schema(
                description = "Семейное положение (не женат / не замужем, женат / замужем, разведен(а), вдовец / вдова)",
                example = "SINGLE",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        MaritalStatus maritalStatus,

        @Schema(
                description = "Число иждивенцев",
                example = "0",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        @PositiveOrZero
        Integer dependentAmount,

        @Valid
        @NotNull
        EmploymentDto employment,

        @Schema(
                description = "Номер счёта",
                example = "RU40817810123456789012",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Pattern(regexp = "[A-Z]{2}\\d{2}[A-Z0-9]{11,30}")
        String accountNumber,

        @Schema(
                description = "Страховка включена",
                example = "false",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        Boolean isInsuranceEnabled,

        @Schema(
                description = "Клиент является зарплатным",
                example = "false",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        Boolean isSalaryClient
) implements MaxAgeValidatable {}