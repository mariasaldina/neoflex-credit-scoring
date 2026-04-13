package com.creditscoring.deal.utils;

import com.creditscoring.deal.dto.calculator.response.CreditDto;
import com.creditscoring.deal.dto.request.EmploymentDto;
import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanOfferDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.entity.Client;
import com.creditscoring.deal.entity.Passport;
import com.creditscoring.deal.enums.EmploymentPosition;
import com.creditscoring.deal.enums.EmploymentStatus;
import com.creditscoring.deal.enums.Gender;
import com.creditscoring.deal.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TestDataFactory {

    public static LoanStatementRequestDto.LoanStatementRequestDtoBuilder createLoanStatementDto() {
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

    public static Client.ClientBuilder createClient() {
        return Client.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .email("test@mail.com")
                .birthdate(LocalDate.now().minusYears(26))
                .passport(Passport.builder()
                        .series("0000")
                        .number("000000")
                        .build());
    }

    public static LoanOfferDto.LoanOfferDtoBuilder createLoanOfferDto() {
        return LoanOfferDto.builder()
                .rate(BigDecimal.valueOf(20))
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(109456.58))
                .monthlyPayment(BigDecimal.valueOf(10939.38))
                .term(10)
                .isInsuranceEnabled(false)
                .isSalaryClient(false);
    }

    public static EmploymentDto.EmploymentDtoBuilder createEmploymentBuilder() {
        return EmploymentDto.builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .employerINN("0000000000")
                .salary(new BigDecimal("70000"))
                .position(EmploymentPosition.WORKER)
                .workExperienceTotal(40)
                .workExperienceCurrent(10);
    }

    public static FinishRegistrationRequestDto.FinishRegistrationRequestDtoBuilder
        createFinishRegistrationDto()
    {
        return FinishRegistrationRequestDto.builder()
                .gender(Gender.MALE)
                .passportIssueDate(LocalDate.now().minusYears(6))
                .passportIssueBranch("ГУ МВД России по г. Москва")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .employment(createEmploymentBuilder().build())
                .accountNumber("RU0000000000000000");
    }

    public static CreditDto.CreditDtoBuilder createCreditDto() {
        return CreditDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(10)
                .monthlyPayment(BigDecimal.valueOf(10939.38))
                .rate(BigDecimal.valueOf(20))
                .psk(BigDecimal.valueOf(109456.58))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .paymentSchedule(List.of());
    }
}
