package com.creditscoring.gateway.api;

import com.creditscoring.gateway.dto.request.CodeDto;
import com.creditscoring.gateway.dto.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Документы", description = "API для работы с документами")
@ApiResponses({
        @ApiResponse(
                responseCode = "404",
                description = "Заявка не найдена",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class)
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "Статус заявки не позволяет выполнить действие",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка сервера",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class)
                )
        )
})
@RequestMapping("/document/{statementId}")
public interface DocumentApi {

    @Operation(
            summary = "Запрос на формирование документов",
            description = "Отправляет на почту документы для подписания " +
                    "и ссылку для подтверждения согласия с условиями"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Документы сформированы, отправка письма запущена"
            )
    })
    @PostMapping
    ResponseEntity<Void> sendCreateDocumentsRequest(
            @PathVariable UUID statementId
    );

    @Operation(
            summary = "Согласие на подписание документов",
            description = "Отправляет на почту код подтверждения и ссылку для подписания документов"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Выдан код подтверждения, создан запрос на отправку письма"
            )
    })
    @PostMapping("/sign")
    ResponseEntity<Void> sendSignDocumentsRequest(
            @PathVariable UUID statementId
    );

    @Operation(
            summary = "Проверка кода подтверждения и выдача кредита",
            description = "Сверяет код, в случае успеха меняет статус кредита на \"выдан\" " +
                    "и отправляет оповещение на почту"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Код подтверждения прошел проверку, кредит одобрен"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Код подтверждения не совпал с сохраненным в заявке",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    @PostMapping("/sign/code")
    ResponseEntity<Void> signDocuments(
            @RequestBody CodeDto dto,
            @PathVariable UUID statementId
    );
}
