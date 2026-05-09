package com.creditscoring.gateway.api;

import com.creditscoring.gateway.dto.LoanOfferDto;
import com.creditscoring.gateway.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.gateway.dto.request.LoanStatementRequestDto;
import com.creditscoring.gateway.dto.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Tag(name = "Заявка", description = "API для работы с заявкой")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Ошибка прескоринга / валидации",
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
@RequestMapping("/statement")
public interface StatementApi {

    @Operation(
            summary = "Прескоринг заявки и запрос кредитных предложений",
            description = "Производит прескоринг, в случае успеха возвращает кредитные предложения для заявки"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Прескоринг пройден, заявка сохранена, кредитные предложения рассчитаны",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class))
                    )
            )
    })
    ResponseEntity<List<LoanOfferDto>> createStatement(
            @Valid @RequestBody LoanStatementRequestDto dto
    );

    @Operation(
            summary = "Применение выбранного кредитного предложения",
            description = "Сохраняет кредитное предложение, " +
                    "отправляет приглашение для завершения оформления на почту"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Заявка обновлена, запущен процесс отправки письма"
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
    ResponseEntity<Void> selectOffer(
            @Valid @RequestBody LoanOfferDto dto
    );

    @Operation(
            summary = "Завершение регистрации и расчет кредитного предложения",
            description = "Происходит скоринг. Если заявка проходит его, " +
                    "рассчитывается и сохраняется полное кредитное предложение. " +
                    "На почту клиенту приходит ссылка для запроса на формирование документов " +
                    "либо отказ."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Заявка прошла скоринг, кредитное предложение сохранено, " +
                            "запущен процесс отправки письма"
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
                    description = "Заявка не прошла скоринг, запущен процесс отправки письма об отказе",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    ResponseEntity<Void> finishRegistration(
            @Valid FinishRegistrationRequestDto dto,
            @PathVariable UUID statementId
    );
}
