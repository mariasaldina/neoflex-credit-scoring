package com.creditscoring.deal.api;

import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanOfferDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.exception.dto.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "МС Сделка", description = "Сохранение данных о клиенте и заявке")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Ошибка валидации",
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
@RequestMapping("/deal")
public interface DealApi {

    @Operation(
            summary = "Создание клиента и его заявки",
            description = "Сохраняет клиента и его паспорт, создаёт заявку, " +
                    "возвращает список кредитных предложений, рассчитанных в МС Calculator"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Клиент и заявка сохранены",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class))
                    )
            )
    })
    @PostMapping("/statement")
    ResponseEntity<List<LoanOfferDto>> createStatement(
            @Valid @RequestBody LoanStatementRequestDto reqBody
    );

    @Operation(
            summary = "Применение выбранного кредитного предложения",
            description = "Обновляет статус заявки (APPROVED), сохраняет примененное предложение"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Заявка обновлена"
            ),
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
                    description = "Статус заявки не позволяет изменить кредитное предложение",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    @PostMapping("/offer/select")
    ResponseEntity<Void> selectOffer(
            @Valid @RequestBody LoanOfferDto reqBody
    );

    @Operation(
            summary = "Завершение регистрации и расчет кредитного предложения",
            description = "По полученным из запроса и БД данным о клиенте и заявке происходит скоринг, " +
                    "рассчитываются полные условия кредита, сохраняются в БД, " +
                    "статус заявки изменяется на CC_DENIED / CC_APPROVED"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Заявка прошла скоринг, кредитное предложение сохранено в БД"
            ),
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
                    description = "Не выбрано кредитное предложение",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Ошибка скоринга",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    @PostMapping("/calculate/{statementId}")
    ResponseEntity<Void> calculateCredit(
            @Valid @RequestBody FinishRegistrationRequestDto reqBody,
            @PathVariable UUID statementId
    );
}
