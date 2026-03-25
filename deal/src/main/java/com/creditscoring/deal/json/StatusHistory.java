package com.creditscoring.deal.json;

import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.ChangeType;

import java.time.LocalDateTime;

public record StatusHistory(
        ApplicationStatus status,
        LocalDateTime time,
        ChangeType changeType
) {

    public StatusHistory(ApplicationStatus status, ChangeType changeType) {
        this(status, LocalDateTime.now(), changeType);
    }
}
