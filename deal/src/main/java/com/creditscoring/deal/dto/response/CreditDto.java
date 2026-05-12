package com.creditscoring.deal.dto.response;

import com.creditscoring.deal.enums.CreditStatus;
import com.creditscoring.deal.json.PaymentScheduleElement;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreditDto(
        UUID creditId,
        BigDecimal amount,
        Integer term,
        BigDecimal monthlyPayment,
        BigDecimal rate,
        BigDecimal psk,
        List<PaymentScheduleElement> paymentSchedule,
        Boolean insuranceEnabled,
        Boolean salaryClient,
        CreditStatus creditStatus
) { }
