package com.creditscoring.deal.dto.response;

import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.json.AppliedOffer;
import com.creditscoring.deal.json.StatusHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record StatementDto(
        UUID statementId,
        ApplicationStatus status,
        LocalDateTime creationDate,
        AppliedOffer appliedOffer,
        LocalDateTime signDate,
        List<StatusHistory> statusHistory,
        ClientDto client,
        CreditDto credit
) {}
