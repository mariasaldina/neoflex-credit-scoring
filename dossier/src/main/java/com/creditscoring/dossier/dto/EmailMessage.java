package com.creditscoring.dossier.dto;

import com.creditscoring.dossier.enums.EmailTheme;

import java.util.UUID;

public record EmailMessage(
        String address,
        EmailTheme theme,
        UUID statementId,
        String text
) {}
