package com.creditscoring.gateway.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Код для подписания кредитного договора")
public record CodeDto(

        @Schema(
                description = "Код подтверждения, введённый пользователем",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @NotNull
        UUID sesCode
) { }
