package com.creditscoring.calculator.service;

import com.creditscoring.calculator.domain.FullPaymentData;
import com.creditscoring.calculator.dto.PaymentScheduleElementDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
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

    public FullPaymentData paymentSchedule(
            Integer term,
            BigDecimal rate,
            BigDecimal amount,
            BigDecimal monthlyPayment,
            LocalDate issueDate
    ) {
        List<PaymentScheduleElementDto> payments = new ArrayList<>();

        LocalDate prevPaymentDate = issueDate;
        BigDecimal debt = amount;
        BigDecimal psk = BigDecimal.ZERO;

        for (int i = 0; i < term; ++i) {
            LocalDate curPaymentDate = prevPaymentDate.plusMonths(1);
            long daysBetweenPayments = ChronoUnit.DAYS.between(prevPaymentDate, curPaymentDate);
            int daysInYear = Year.from(curPaymentDate).length();

            BigDecimal interestPayment = debt
                    .multiply(rate)
                    .multiply(new BigDecimal(daysBetweenPayments))
                    .divide(
                            new BigDecimal(daysInYear).multiply(new BigDecimal("100")),
                            10,
                            RoundingMode.HALF_UP
                    );

            BigDecimal totalPayment = i != term - 1
                    ? monthlyPayment
                    : interestPayment.add(debt).setScale(2, RoundingMode.HALF_UP);
            BigDecimal debtPayment = totalPayment.subtract(interestPayment);
            debt = i != term - 1
                    ? debt.subtract(debtPayment)
                    : BigDecimal.ZERO;
            prevPaymentDate = curPaymentDate;

            payments.add(new PaymentScheduleElementDto(
                    i + 1,
                    prevPaymentDate,
                    totalPayment.setScale(2, RoundingMode.HALF_UP),
                    interestPayment.setScale(2, RoundingMode.HALF_UP),
                    debtPayment.setScale(2, RoundingMode.HALF_UP),
                    debt.setScale(2, RoundingMode.HALF_UP)
            ));

            psk = psk.add(totalPayment);
        }

        return new FullPaymentData(payments, psk);
    }
}
