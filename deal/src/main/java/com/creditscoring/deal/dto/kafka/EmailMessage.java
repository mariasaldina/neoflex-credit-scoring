package com.creditscoring.deal.dto.kafka;

import com.creditscoring.deal.enums.EmailTheme;

import java.util.UUID;

public record EmailMessage(
        String address,
        EmailTheme theme,
        UUID statementId,
        String text
) {}
