package com.creditscoring.gateway.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Унифицированный формат ошибки")
public record ApiError(

        @Schema(
                description = "id ошибки (для поиска в логах)",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID id,

        @Schema(
                description = "HTTP статус",
                example = "INTERNAL_SERVER_ERROR"
        )
        HttpStatusCode status,

        @Schema(
                description = "Сообщение",
                example = "Внутренняя ошибка сервера"
        )
        String message,

        @Schema(
                description = "Детали",
                example = "[]"
        )
        List<String> details,

        @Schema(
                description = "Временная метка",
                example = "2026-05-09T09:27:27.530027679Z"
        )
        Instant timestamp
) {
    public ApiError(HttpStatusCode status, String message, List<String> details) {
        this(UUID.randomUUID(), status, message, details, Instant.now());
    }
}