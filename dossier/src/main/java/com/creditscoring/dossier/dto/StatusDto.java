package com.creditscoring.dossier.dto;

import com.creditscoring.dossier.enums.ApplicationStatus;

public record StatusDto(
        ApplicationStatus status
) { }
