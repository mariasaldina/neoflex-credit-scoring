package com.creditscoring.deal.entity;

import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.ChangeType;
import com.creditscoring.deal.enums.CreditStatus;
import com.creditscoring.deal.json.AppliedOffer;
import com.creditscoring.deal.json.StatusHistory;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Statement {

    public Statement(Client client) {
        this.client = client;
        this.creationDate = LocalDateTime.now();
        changeStatus(ApplicationStatus.PREAPPROVAL);
    }

    public void changeStatus(ApplicationStatus status) {
        this.changeStatus(status, ChangeType.AUTOMATIC);
    }

    public void changeStatus(ApplicationStatus status, ChangeType changeType) {
        this.status = status;
        statusHistory.add(new StatusHistory(status, changeType));
    }

    public void applyOffer(AppliedOffer offer) {
        changeStatus(ApplicationStatus.APPROVED);
        this.appliedOffer = offer;
    }

    public void saveCredit(Credit credit) {
        changeStatus(ApplicationStatus.CC_APPROVED);
        this.credit = credit;
    }

    public void sign() {
        this.signDate = LocalDateTime.now();
        this.credit.setCreditStatus(CreditStatus.ISSUED);
        changeStatus(ApplicationStatus.CREDIT_ISSUED);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID statementId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private AppliedOffer appliedOffer;

    private LocalDateTime signDate;
    private UUID sesCode;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<StatusHistory> statusHistory = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id")
    private Credit credit;
}
