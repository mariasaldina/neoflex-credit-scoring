package com.creditscoring.deal.api;

import com.creditscoring.deal.dto.request.StatusDto;
import com.creditscoring.deal.exception.dto.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PutMapping("/status")
    ResponseEntity<Void> updateStatus(
            @RequestBody StatusDto statusDto,
            @PathVariable UUID statementId
    );
}
