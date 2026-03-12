package com.creditscoring.calculator;

import com.creditscoring.calculator.dto.EmploymentDto;
import com.creditscoring.calculator.dto.ScoringDataDto;
import com.creditscoring.calculator.enums.EmploymentStatus;
import com.creditscoring.calculator.enums.Gender;
import com.creditscoring.calculator.enums.MaritalStatus;
import com.creditscoring.calculator.enums.Position;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public class TestDataFactory {

    public static ScoringDataDto.ScoringDataDtoBuilder createScoringDataBuilder() {
        return ScoringDataDto.builder()
                .amount(new BigDecimal("100000"))
                .term(10)
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .gender(Gender.MALE)
                .birthdate(LocalDate.of(2000, Month.MARCH, 9))
                .passportSeries("0000")
                .passportNumber("000000")
                .passportIssueDate(LocalDate.of(2020, Month.APRIL, 12))
                .passportIssueBranch("ГУ МВД России по г. Москва")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .employment(createEmploymentBuilder().build())
                .accountNumber("RU0000000000000000")
                .isInsuranceEnabled(false)
                .isSalaryClient(false);
    }

    public static EmploymentDto.EmploymentDtoBuilder createEmploymentBuilder() {
        return EmploymentDto.builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .employerINN("0000000000")
                .salary(new BigDecimal("70000"))
                .position(Position.WORKER)
                .workExperienceTotal(40)
                .workExperienceCurrent(10);
    }
}