package com.creditscoring.deal.api;

import com.creditscoring.deal.dto.request.StatusDto;
import com.creditscoring.deal.dto.response.StatementDto;
import com.creditscoring.deal.exception.dto.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Инструменты для работы с заявкой", description = "Получение заявки и изменение её статуса")
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
                responseCode = "500",
                description = "Внутренняя ошибка сервера",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class)
                )
        )
})
@RequestMapping("/deal/admin/statement")
public interface DealAdminApi {

    @Operation(
            summary = "Изменение статуса заявки",
            description = "Меняет статус заявки"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Статус заявки изменён"
            )
    })
    @PutMapping("/{statementId}/status")
    ResponseEntity<Void> updateStatus(
            @RequestBody StatusDto statusDto,
            @PathVariable UUID statementId
    );

    @Operation(
            summary = "Получение заявки",
            description = "Возвращает заявку"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Заявка получена"
            )
    })
    @GetMapping("/{statementId}")
    ResponseEntity<StatementDto> getStatement(
            @PathVariable UUID statementId
    );

    @Operation(
            summary = "Получение всех заявок",
            description = "Возвращает все заявки"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Заявки получены"
            )
    })
    @GetMapping
    ResponseEntity<List<StatementDto>> getAllStatements();
}
