package com.creditscoring.gateway.dto.request;

import com.creditscoring.gateway.enums.Gender;
import com.creditscoring.gateway.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Данные для завершения регистрации и расчёта финального кредитного предложения")
public record FinishRegistrationRequestDto(

        @Schema(
                description = "Пол",
                example = "MALE"
        )
        @NotNull
        Gender gender,

        @Schema(
                description = "Семейное положение (не женат / не замужем, женат / замужем, разведен(а), вдовец / вдова)",
                example = "SINGLE"
        )
        @NotNull
        MaritalStatus maritalStatus,

        @Schema(
                description = "Число иждивенцев",
                example = "0"
        )
        @NotNull
        @PositiveOrZero
        Integer dependentAmount,

        @Schema(
                description = "Дата выдачи паспорта в формате ISO 8601",
                example = "2020-01-01"
        )
        @NotNull
        @Past
        LocalDate passportIssueDate,

        @Schema(
                description = "Орган выдачи паспорта",
                example = "ГУ МВД России по г. Москва"
        )
        @NotBlank
        String passportIssueBranch,

        @Valid
        @NotNull
        EmploymentDto employment,

        @Schema(
                description = "Номер счёта",
                example = "RU40817810123456789012"
        )
        @NotBlank
        @Pattern(regexp = "[A-Z]{2}\\d{2}[A-Z0-9]{11,30}")
        String accountNumber
) {}