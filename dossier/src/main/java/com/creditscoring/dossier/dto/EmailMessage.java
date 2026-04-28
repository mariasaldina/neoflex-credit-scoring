package com.creditscoring.dossier.dto;

import com.creditscoring.dossier.enums.EmailTheme;
import lombok.Builder;

import java.util.UUID;

@Builder
public record EmailMessage(
        String address,
        EmailTheme theme,
        UUID statementId,
        String text
) {}
