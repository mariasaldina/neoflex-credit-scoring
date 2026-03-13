package com.creditscoring.calculator;

import com.creditscoring.calculator.configuration.ScoringProperties;
import com.creditscoring.calculator.dto.ScoringDataDto;
import com.creditscoring.calculator.enums.EmploymentStatus;
import com.creditscoring.calculator.enums.Gender;
import com.creditscoring.calculator.enums.MaritalStatus;
import com.creditscoring.calculator.enums.Position;
import com.creditscoring.calculator.exceptions.ScoringException;
import com.creditscoring.calculator.service.ScoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

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
    void employmentStatusTest_success(EmploymentStatus status, BigDecimal expectedRate) {
        BigDecimal rate = scoringService.scoring(
                new BigDecimal("20"),
                TestDataFactory.createScoringDataBuilder()
                        .employment(TestDataFactory.createEmploymentBuilder()
                                .employmentStatus(status)
                                .build())
                        .build()
        );
        assertThat(rate).isEqualByComparingTo(expectedRate);
    }

    @Test
    void employmentStatusTest_failure() {
        ScoringDataDto scoringData = TestDataFactory.createScoringDataBuilder()
                .employment(TestDataFactory.createEmploymentBuilder()
                        .employmentStatus(EmploymentStatus.UNEMPLOYED)
                        .build())
                .build();

        assertThatThrownBy(() -> scoringService.scoring(new BigDecimal("20"), scoringData))
                .isInstanceOf(ScoringException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "MIDDLE_MANAGER, 18",
            "TOP_MANAGER, 17"
    })
    void employmentPositionTest_success(Position position, BigDecimal expectedRate) {
        BigDecimal rate = scoringService.scoring(
                new BigDecimal("20"),
                TestDataFactory.createScoringDataBuilder()
                        .employment(TestDataFactory.createEmploymentBuilder()
                                .position(position)
                                .build())
                        .build()
        );
        assertThat(rate).isEqualByComparingTo(expectedRate);
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

        assertDoesNotThrow(() -> scoringService.scoring(new BigDecimal("20"), scoringData));
    }

    @Test
    void salaryTest_failure() {
        ScoringDataDto scoringData = TestDataFactory.createScoringDataBuilder()
                .amount(new BigDecimal("1000000"))
                .employment(TestDataFactory.createEmploymentBuilder()
                        .salary(new BigDecimal("40000"))
                        .build())
                .build();

        assertThatThrownBy(() -> scoringService.scoring(new BigDecimal("20"), scoringData))
                .isInstanceOf(ScoringException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "MARRIED, 17",
            "DIVORCED, 21"
    })
    void maritalStatusTest_success(MaritalStatus status, BigDecimal expectedRate) {
        BigDecimal rate = scoringService.scoring(
                new BigDecimal("20"),
                TestDataFactory.createScoringDataBuilder()
                        .maritalStatus(status)
                        .build()
        );
        assertThat(rate).isEqualByComparingTo(expectedRate);
    }

    @ParameterizedTest
    @CsvSource({
            "MALE, 20, 20",
            "MALE, 31, 17",
            "FEMALE, 20, 20",
            "FEMALE, 59, 17"
    })
    void demographicsTest_success(Gender gender, Integer age, BigDecimal expectedRate) {
        BigDecimal rate = scoringService.scoring(
                new BigDecimal("20"),
                TestDataFactory.createScoringDataBuilder()
                        .gender(gender)
                        .birthdate(LocalDate.now().minusYears(age))
                        .build()
        );
        assertThat(rate).isEqualByComparingTo(expectedRate);
    }

    @Test
    void totalWorkExperienceTest_success() {
        ScoringDataDto scoringData = TestDataFactory.createScoringDataBuilder()
                .employment(TestDataFactory.createEmploymentBuilder()
                        .workExperienceTotal(20)
                        .build())
                .build();
        assertDoesNotThrow(() -> scoringService.scoring(new BigDecimal("20"), scoringData));
    }

    @Test
    void totalWorkExperienceTest_failure() {
        ScoringDataDto scoringData = TestDataFactory.createScoringDataBuilder()
                .employment(TestDataFactory.createEmploymentBuilder()
                        .workExperienceTotal(17)
                        .build())
                .build();
        assertThatThrownBy(() -> scoringService.scoring(new BigDecimal("20"), scoringData))
                .isInstanceOf(ScoringException.class);
    }

    @Test
    void currentWorkExperienceTest_success() {
        ScoringDataDto scoringData = TestDataFactory.createScoringDataBuilder()
                .employment(TestDataFactory.createEmploymentBuilder()
                        .workExperienceCurrent(4)
                        .build())
                .build();
        assertDoesNotThrow(() -> scoringService.scoring(new BigDecimal("20"), scoringData));
    }

    @Test
    void currentWorkExperienceTest_failure() {
        ScoringDataDto scoringData = TestDataFactory.createScoringDataBuilder()
                .employment(TestDataFactory.createEmploymentBuilder()
                        .workExperienceCurrent(2)
                        .build())
                .build();
        assertThatThrownBy(() -> scoringService.scoring(new BigDecimal("20"), scoringData))
                .isInstanceOf(ScoringException.class);
    }
}
