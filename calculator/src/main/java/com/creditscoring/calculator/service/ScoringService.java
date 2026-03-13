package com.creditscoring.calculator.service;

import com.creditscoring.calculator.configuration.ScoringProperties;
import com.creditscoring.calculator.dto.ScoringDataDto;
import com.creditscoring.calculator.enums.EmploymentStatus;
import com.creditscoring.calculator.enums.Gender;
import com.creditscoring.calculator.enums.MaritalStatus;
import com.creditscoring.calculator.enums.Position;
import com.creditscoring.calculator.exceptions.ScoringException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@Service
public class ScoringService {
    private final ScoringProperties scoringProperties;
    private final Logger logger = LoggerFactory.getLogger(ScoringService.class);

    public ScoringService(
            ScoringProperties scoringProperties
    ) {
        this.scoringProperties = scoringProperties;
    }

    private BigDecimal employmentStatusRule(EmploymentStatus status) {
        BigDecimal rate;
        switch (status) {
            case SELF_EMPLOYED -> { rate = scoringProperties.employment().status().selfEmployedRate(); }
            case BUSINESS_OWNER -> { rate = scoringProperties.employment().status().businessOwnerRate(); }
            case UNEMPLOYED -> { throw new ScoringException("Заёмщик должен быть трудоустроен"); }
            default -> { rate = BigDecimal.ZERO; }
        }
        logger.debug("\nВлияние рабочего статуса на ставку: {}\n", rate);
        return rate;
    }

    private BigDecimal employmentPositionRule(Position position) {
        BigDecimal rate;
        switch (position) {
            case MIDDLE_MANAGER -> { rate = scoringProperties.employment().position().middleManagerRate(); }
            case TOP_MANAGER -> { rate = scoringProperties.employment().position().topManagerRate(); }
            default -> { rate = BigDecimal.ZERO; }
        }
        logger.debug("\nВлияние позиции на работе на ставку: {}\n", rate);
        return rate;
    }

    private BigDecimal salaryRule(BigDecimal salary, BigDecimal amount) {
        BigDecimal ratio = scoringProperties.employment().maxLoanToSalariesRatio();
        if (amount
                .divide(salary, 10, RoundingMode.HALF_UP)
                .compareTo(ratio) > 0) {
            throw new ScoringException(
                    String.format("Сумма займа превышает %s заработные платы", ratio)
            );
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal maritalStatusRule(MaritalStatus status) {
        BigDecimal rate;
        switch (status) {
            case MARRIED -> { rate = scoringProperties.maritalStatus().marriedRate(); }
            case DIVORCED -> { rate = scoringProperties.maritalStatus().divorcedRate(); }
            default -> { rate = BigDecimal.ZERO; }
        }
        logger.debug("\nВлияние семейного положения на ставку: {}\n", rate);
        return rate;
    }

    private BigDecimal demographicsRule(Gender gender, Integer age) {
        return scoringProperties.demographics().stream()
                .filter(rule -> rule.gender() == null || rule.gender() == gender)
                .filter(rule -> rule.minAge() == null || age >= rule.minAge())
                .filter(rule -> rule.maxAge() == null || age <= rule.maxAge())
                .peek(rule ->
                        logger.debug("\nПрименено демографическое правило {}\n", rule))
                .map(ScoringProperties.DemographicRule::rate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal totalWorkExperienceRule(Integer experience) {
        if (experience < scoringProperties.employment().minTotalWorkExperience()) {
            throw new ScoringException("Общий стаж работы слишком маленький");
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal currentWorkExperienceRule(Integer experience) {
        if (experience < scoringProperties.employment().minCurrentWorkExperience()) {
            throw new ScoringException("Стаж на текущем месте работы слишком маленький");
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal scoring(BigDecimal rate, ScoringDataDto scoringData) {
        Integer age = Period.between(scoringData.birthdate(), LocalDate.now()).getYears();

        return rate
                .add(employmentStatusRule(scoringData.employment().employmentStatus()))
                .add(employmentPositionRule(scoringData.employment().position()))
                .add(salaryRule(scoringData.employment().salary(), scoringData.amount()))
                .add(maritalStatusRule(scoringData.maritalStatus()))
                .add(demographicsRule(scoringData.gender(), age))
                .add(totalWorkExperienceRule(scoringData.employment().workExperienceTotal()))
                .add(currentWorkExperienceRule(scoringData.employment().workExperienceCurrent()));
    }
}
