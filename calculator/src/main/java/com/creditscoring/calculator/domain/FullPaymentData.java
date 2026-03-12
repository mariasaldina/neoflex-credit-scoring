package com.creditscoring.calculator.domain;

import com.creditscoring.calculator.dto.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.util.List;

public record FullPaymentData(
        List<PaymentScheduleElementDto> paymentSchedule,
        BigDecimal psk
) {}