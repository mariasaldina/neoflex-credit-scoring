package com.creditscoring.deal.entity;

import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.json.AppliedOffer;
import com.creditscoring.deal.json.StatusHistory;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Statement {

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
    private String sesCode;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private StatusHistory statusHistory;
}
