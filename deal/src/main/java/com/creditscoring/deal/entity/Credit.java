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

    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<PaymentScheduleElement> paymentSchedule;

    private Boolean insuranceEnabled;
    private Boolean salaryClient;

    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;
}
