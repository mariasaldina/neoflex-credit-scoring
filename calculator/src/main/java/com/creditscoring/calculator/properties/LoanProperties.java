package com.creditscoring.calculator.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "loan")
public record LoanProperties(

        BigDecimal baseRate,
        Insurance insurance,
        SalaryClient salaryClient

) {

    public record Insurance(
            BigDecimal pricePercent,
            BigDecimal rate
    ) {}

    public record SalaryClient(
            BigDecimal rate
    ) {}
}