package com.creditscoring.deal.dto;

import com.creditscoring.deal.dto.request.EmploymentDto;
import com.creditscoring.deal.enums.Gender;
import com.creditscoring.deal.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Данные для скоринга и расчёта полной стоимости кредита")
public record ScoringDataDto(

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
                description = "Пол",
                example = "MALE",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Gender gender,

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
        String passportNumber,

        @Schema(
                description = "Дата выдачи паспорта в формате ISO 8601",
                example = "2020-01-01",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDate passportIssueDate,

        @Schema(
                description = "Орган выдачи паспорта",
                example = "ГУ МВД России по г. Москва",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String passportIssueBranch,

        @Schema(
                description = "Семейное положение (не женат / не замужем, женат / замужем, разведен(а), вдовец / вдова)",
                example = "SINGLE",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        MaritalStatus maritalStatus,

        @Schema(
                description = "Число иждивенцев",
                example = "0",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer dependentAmount,

        EmploymentDto employment,

        @Schema(
                description = "Номер счёта",
                example = "RU40817810123456789012",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String accountNumber,

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