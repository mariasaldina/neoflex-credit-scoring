package com.creditscoring.calculator.utils;

import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

public class InvalidRequestFactory {
    public static Stream<Arguments> loanOffers() {
        return Stream.of(
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .amount(new BigDecimal("10000"))
                                .build(),
                        "amount"
                ),
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .amount(new BigDecimal("10000001"))
                                .build(),
                        "amount"
                ),
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .term(-1)
                                .build(),
                        "term"
                ),
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .term(2)
                                .build(),
                        "term"
                ),
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .firstName("")
                                .build(),
                        "firstName"
                ),
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .lastName("a".repeat(31))
                                .build(),
                        "lastName"
                ),
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .email("test.mail.com")
                                .build(),
                        "email"
                ),
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .birthdate(LocalDate.now().minusYears(17))
                                .build(),
                        "birthdate"
                ),
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .birthdate(LocalDate.now().minusYears(72))
                                .term(48)
                                .build(),
                        "loanStatementRequestDto"
                ),
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .passportSeries("00000")
                                .build(),
                        "passportSeries"
                ),
                Arguments.of(
                        TestDataFactory.createLoanStatementBuilder()
                                .passportNumber("a00000")
                                .build(),
                        "passportNumber"
                )
        );
    }

    public static Stream<Arguments> scoringData() {
        return Stream.of(
                Arguments.of(
                        TestDataFactory.createScoringDataBuilder()
                                .passportIssueDate(LocalDate.now().plusMonths(1))
                                .build(),
                        "passportIssueDate"
                ),
                Arguments.of(
                        TestDataFactory.createScoringDataBuilder()
                                .maritalStatus(null)
                                .build(),
                        "maritalStatus"
                ),
                Arguments.of(
                        TestDataFactory.createScoringDataBuilder()
                                .employment(
                                        TestDataFactory.createEmploymentBuilder()
                                                .salary(new BigDecimal("-1"))
                                                .build()
                                )
                                .build(),
                        "employment.salary"
                )
        );
    }
}
