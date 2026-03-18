package com.creditscoring.calculator.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "prescoring")
public record PrescoringProperties(
        
        Amount amount,
        Term term,
        Age age
        
) {
    public record Amount(
        BigDecimal min,
        BigDecimal max
    ) {}
    
    public record Term(
        Integer min,
        Integer max
    ) {}
    
    public record Age(
        Integer min,
        Integer max
    ) {}
}
