package com.creditscoring.deal.dto.request;

import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.ChangeType;

public record StatusDto(
        ApplicationStatus status,
        ChangeType changeType
) { }
