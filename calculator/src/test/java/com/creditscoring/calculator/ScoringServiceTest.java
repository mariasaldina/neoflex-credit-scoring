package com.creditscoring.calculator;

import com.creditscoring.calculator.configuration.ScoringProperties;
import com.creditscoring.calculator.dto.ScoringDataDto;
import com.creditscoring.calculator.enums.EmploymentStatus;
import com.creditscoring.calculator.enums.Gender;
import com.creditscoring.calculator.enums.Position;
import com.creditscoring.calculator.service.ScoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoringServiceTest {

    private ScoringService scoringService;

    @BeforeEach
    void setup() {
        ScoringProperties scoringProperties = new ScoringProperties(
                new ScoringProperties.Employment(
                        new ScoringProperties.Employment.Status(
                                new BigDecimal("2"),
                                new BigDecimal("1")
                        ),
                        new ScoringProperties.Employment.Position(
                                new BigDecimal("-2"),
                                new BigDecimal("-3")
                        ),
                        new BigDecimal("24"),
                        18,
                        3
                ),
                new ScoringProperties.MaritalStatus(
                        new BigDecimal("-3"),
                        new BigDecimal("1")
                ),
                List.of(
                        new ScoringProperties.DemographicRule(
                                Gender.MALE,
                                30,
                                55,
                                new BigDecimal("-3")
                        ),
                        new ScoringProperties.DemographicRule(
                                Gender.FEMALE,
                                32,
                                60,
                                new BigDecimal("-3")
                        )
                )
        );

        scoringService = new ScoringService(scoringProperties);
    }

    @ParameterizedTest
    @CsvSource({
            "SELF_EMPLOYED, 22",
            "BUSINESS_OWNER, 21"
    })
    void employmentStatusTest(EmploymentStatus status, BigDecimal expectedRate) {
        BigDecimal rate = scoringService.scoring(
                new BigDecimal("20"),
                TestDataFactory.createScoringDataBuilder()
                        .employment(TestDataFactory.createEmploymentBuilder()
                                .employmentStatus(status)
                                .build())
                        .build()
        );
        assertTrue(expectedRate.compareTo(rate) == 0);
    }

    @ParameterizedTest
    @CsvSource({
            "MIDDLE_MANAGER, 18",
            "TOP_MANAGER, 17"
    })
    void employmentPositionTest(Position position, BigDecimal expectedRate) {
        BigDecimal rate = scoringService.scoring(
                new BigDecimal("20"),
                TestDataFactory.createScoringDataBuilder()
                        .employment(TestDataFactory.createEmploymentBuilder()
                                .position(position)
                                .build())
                        .build()
        );
        assertTrue(expectedRate.compareTo(rate) == 0);
    }

    @ParameterizedTest
    @CsvSource({
            "100000, 70000"
    })
    void salaryTest_success(BigDecimal amount, BigDecimal salary) {
        ScoringDataDto scoringData = TestDataFactory.createScoringDataBuilder()
                .amount(amount)
                .employment(TestDataFactory.createEmploymentBuilder()
                        .salary(salary)
                        .build())
                .build();
        BigDecimal rate = scoringService.scoring(new BigDecimal("20"), scoringData);

        assertTrue(new BigDecimal("20").compareTo(rate) == 0);
    }
}
