package com.creditscoring.calculator.service;

import com.creditscoring.calculator.dto.PaymentScheduleElementDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnnuityModelService {
    public BigDecimal monthlyPayment(
            BigDecimal amount,
            BigDecimal rate,
            Integer term
    ) {
        BigDecimal monthlyRate = rate.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);
        BigDecimal rateMultiplier = monthlyRate.add(BigDecimal.ONE).pow(term);

        return amount
                .multiply(monthlyRate.multiply(rateMultiplier)
                        .divide(rateMultiplier.subtract(BigDecimal.ONE), 10, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public List<PaymentScheduleElementDto> paymentSchedule(
            Integer term,
            BigDecimal rate,
            BigDecimal monthlyPayment,
            LocalDate start
    ) {
        List<PaymentScheduleElementDto> payments = new ArrayList<>();

        LocalDate date = start;
        BigDecimal debt = monthlyPayment.multiply(new BigDecimal(term));
        BigDecimal monthlyRate = rate.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);
        for (int i = 0; i < term; ++i) {
            BigDecimal interestPayment = debt.multiply(monthlyRate);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            debt = debt.subtract(monthlyPayment);
            date = date.plusMonths(1);

            payments.add(new PaymentScheduleElementDto(
                    i + 1,
                    date,
                    monthlyPayment,
                    interestPayment,
                    debtPayment,
                    debt
            ));
        }

        return payments;
    }
}
