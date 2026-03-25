package com.creditscoring.deal.dto.request;

import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.ChangeType;

import java.time.LocalDateTime;

public record StatementStatusHistoryDto (
        ApplicationStatus status,
        LocalDateTime time,
        ChangeType changeType
) {}