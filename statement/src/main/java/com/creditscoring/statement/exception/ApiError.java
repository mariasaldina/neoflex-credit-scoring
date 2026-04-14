package com.creditscoring.statement.exception;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ApiError(
        UUID id,
        Integer status,
        String message,
        List<String> details,
        Instant timestamp
) {}