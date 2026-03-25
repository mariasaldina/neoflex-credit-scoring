package com.creditscoring.deal.json;

import java.math.BigDecimal;
import java.util.UUID;

public record AppliedOffer(
        UUID statementId,
        BigDecimal requestedAmount,
        BigDecimal totalAmount,
        Integer term,
        BigDecimal monthlyPayment,
        BigDecimal rate,
        Boolean isInsuranceEnabled,
        Boolean isSalaryClient
) {}