package com.creditscoring.calculator.utils;

import com.creditscoring.calculator.dto.EmploymentDto;
import com.creditscoring.calculator.dto.LoanStatementRequestDto;
import com.creditscoring.calculator.dto.ScoringDataDto;
import com.creditscoring.calculator.enums.EmploymentStatus;
import com.creditscoring.calculator.enums.Gender;
import com.creditscoring.calculator.enums.MaritalStatus;
import com.creditscoring.calculator.enums.Position;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestDataFactory {

    public static ScoringDataDto.ScoringDataDtoBuilder createScoringDataBuilder() {
        return ScoringDataDto.builder()
                .amount(new BigDecimal("100000"))
                .term(10)
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .gender(Gender.MALE)
                .birthdate(LocalDate.now().minusYears(26))
                .passportSeries("0000")
                .passportNumber("000000")
                .passportIssueDate(LocalDate.now().minusYears(6))
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

    public static LoanStatementRequestDto.LoanStatementRequestDtoBuilder createLoanStatementBuilder() {
        return LoanStatementRequestDto.builder()
                .amount(new BigDecimal("100000"))
                .term(10)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .email("test@mail.com")
                .birthdate(LocalDate.now().minusYears(26))
                .passportSeries("0000")
                .passportNumber("000000");
    }
}