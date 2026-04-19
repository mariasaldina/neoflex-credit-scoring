package com.creditscoring.statement.utils;

import com.creditscoring.statement.dto.LoanOfferDto;
import com.creditscoring.statement.dto.request.LoanStatementRequestDto;
import com.creditscoring.statement.exception.ApiError;
import org.springframework.http.HttpStatusCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TestDataFactory {

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

    public static ApiError createApiError(HttpStatusCode status, String message, List<String> details) {
        return new ApiError(UUID.randomUUID(), status.value(), message, details, Instant.now());
    }
}