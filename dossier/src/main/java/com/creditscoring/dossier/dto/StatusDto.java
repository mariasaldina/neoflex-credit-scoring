package com.creditscoring.dossier.dto;

import com.creditscoring.dossier.enums.ApplicationStatus;
import com.creditscoring.dossier.enums.ChangeType;

public record StatusDto(
        ApplicationStatus status,
        ChangeType changeType
) { }
