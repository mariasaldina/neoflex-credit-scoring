package com.creditscoring.deal.entity;

import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.ChangeType;
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
        this.status = ApplicationStatus.PREAPPROVAL;
        this.creationDate = LocalDateTime.now();
        this.statusHistory = new ArrayList<>();
        statusHistory.add(new StatusHistory(
                ApplicationStatus.PREAPPROVAL,
                ChangeType.AUTOMATIC
        ));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID statementId;

    private UUID creditId;

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
    private List<StatusHistory> statusHistory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}
