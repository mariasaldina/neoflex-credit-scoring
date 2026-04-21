package com.creditscoring.deal.api;

import com.creditscoring.deal.dto.request.CodeDto;
import com.creditscoring.deal.dto.request.LoanOfferDto;
import com.creditscoring.deal.exception.dto.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Подписание заявки", description = "Работа с документами и изменение статуса заявки")
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
@RestController
@RequestMapping("/deal/document/{statementId}")
public interface DealDocumentApi {

    @Operation(
            summary = "Запрос на формирование документов",
            description = "Меняет статус заявки на SEND_DOCUMENTS и" +
                    "запрашивает отправку письма с документами для подписи"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Создан запрос на отправку письма"
            )
    })
    @PostMapping("/send")
    ResponseEntity<Void> sendDocumentRequest(
            @PathVariable UUID statementId
    );

    @Operation(
            summary = "Согласие на подпись документов",
            description = "Сохраняет код сессии в заявку, " +
                    "запрашивает отправку письма с кодом сессии и ссылкой для подписания документов"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Выдан код сессии, создан запрос на отправку письма"
            )
    })
    @PostMapping("/sign")
    ResponseEntity<Void> signDocumentRequest(
            @PathVariable UUID statementId
    );

    @Operation(
            summary = "Проверка кода сессии и выдача кредита",
            description = "Меняет статус заявки на DOCUMENTS_SIGNED, затем CREDIT_ISSUED, " +
                    "запрашивает отправку письма, оповещающего об успешной выдаче кредита"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Код сессии прошел проверку, кредит одобрен"
            )
    })
    @PostMapping("/code")
    ResponseEntity<Void> verifySesCode(
            @RequestBody CodeDto codeDto,
            @PathVariable UUID statementId
    );
}
