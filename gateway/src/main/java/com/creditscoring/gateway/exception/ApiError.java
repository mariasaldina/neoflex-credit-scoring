package com.creditscoring.gateway.exception;

import org.springframework.http.HttpStatusCode;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ApiError(
        UUID id,
        HttpStatusCode status,
        String message,
        List<String> details,
        Instant timestamp
) {
    public ApiError(HttpStatusCode status, String message, List<String> details) {
        this(UUID.randomUUID(), status, message, details, Instant.now());
    }
}