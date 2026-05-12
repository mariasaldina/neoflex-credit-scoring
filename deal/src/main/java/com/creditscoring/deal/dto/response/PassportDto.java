package com.creditscoring.deal.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record PassportDto(
        UUID passportId,
        String series,
        String number,
        String issueBranch,
        LocalDate issueDate
) {}
