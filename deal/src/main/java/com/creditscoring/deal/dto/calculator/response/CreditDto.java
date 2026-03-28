package com.creditscoring.deal.dto.calculator.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CreditDto(
        BigDecimal amount,
        Integer term,
        BigDecimal monthlyPayment,
        BigDecimal rate,
        BigDecimal psk,
        Boolean isInsuranceEnabled,
        Boolean isSalaryClient,
        List<PaymentScheduleElementDto> paymentSchedule
) {}