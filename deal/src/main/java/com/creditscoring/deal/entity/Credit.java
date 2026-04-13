package com.creditscoring.deal.entity;

import com.creditscoring.deal.enums.CreditStatus;
import com.creditscoring.deal.json.PaymentScheduleElement;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID creditId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer term;

    @Column(nullable = false)
    private BigDecimal monthlyPayment;

    @Column(nullable = false)
    private BigDecimal rate;

    @Column(nullable = false)
    private BigDecimal psk;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<PaymentScheduleElement> paymentSchedule;

    @Column(nullable = false)
    private Boolean insuranceEnabled;

    @Column(nullable = false)
    private Boolean salaryClient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditStatus creditStatus;
}
