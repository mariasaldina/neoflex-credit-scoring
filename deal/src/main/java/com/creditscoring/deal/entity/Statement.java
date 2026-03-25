package com.creditscoring.deal.entity;

import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.json.AppliedOffer;
import com.creditscoring.deal.json.StatusHistory;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Statement {

    public Statement(UUID clientId) {
        this.clientId = clientId;
        this.status = ApplicationStatus.PREAPPROVAL;
        this.creationDate = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID statementId;

    private UUID clientId;
    private UUID creditId;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private LocalDateTime creationDate;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private AppliedOffer appliedOffer;

    private LocalDateTime signDate;
    private UUID sesCode;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<StatusHistory> statusHistory = new ArrayList<>();
}
