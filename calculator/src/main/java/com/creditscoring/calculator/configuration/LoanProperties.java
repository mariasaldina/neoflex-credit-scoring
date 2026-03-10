package com.creditscoring.calculator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "loan")
@Data
public class LoanProperties {
    private BigDecimal baseRate;
    private Insurance insurance;
    private SalaryClient salaryClient;

    @Data
    public static class Insurance {
        private BigDecimal pricePercent;
        private BigDecimal rateReduction;
    }

    @Data
    public static class SalaryClient {
        private BigDecimal rateReduction;
    }
}