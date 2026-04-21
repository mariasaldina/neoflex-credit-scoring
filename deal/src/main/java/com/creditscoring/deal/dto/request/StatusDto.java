package com.creditscoring.deal.dto.request;

import com.creditscoring.deal.enums.ApplicationStatus;

public record StatusDto(
        ApplicationStatus status
) { }
