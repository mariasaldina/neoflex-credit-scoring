package com.creditscoring.deal.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "calculator")
public record CalculatorServiceProperties(
        String url
) {}
