package com.creditscoring.calculator.properties;

import com.creditscoring.calculator.enums.Gender;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.List;

@ConfigurationProperties(prefix = "scoring")
public record ScoringProperties (

        Employment employment,
        MaritalStatus maritalStatus,
        List<DemographicRule> demographics

) {

    public record Employment(

            Status status,
            Position position,
            BigDecimal maxLoanToSalariesRatio,
            Integer minTotalWorkExperience,
            Integer minCurrentWorkExperience

    ) {
        public record Status(
                BigDecimal selfEmployedRate,
                BigDecimal businessOwnerRate
        ) {}

        public record Position(
                BigDecimal middleManagerRate,
                BigDecimal topManagerRate
        ) {}
    }

    public record MaritalStatus(
            BigDecimal marriedRate,
            BigDecimal divorcedRate
    ) {}

    public record DemographicRule(
            Gender gender,
            Integer minAge,
            Integer maxAge,
            BigDecimal rate
    ) {}
}
