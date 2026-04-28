package com.creditscoring.statement.api;

import com.creditscoring.statement.dto.LoanOfferDto;
import com.creditscoring.statement.dto.request.LoanStatementRequestDto;
import com.creditscoring.statement.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Обработчик кредитной заявки", description = "API для прескоринга и расчёта кредитных предложений")
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
    ResponseEntity<List<LoanOfferDto>> prescoring(
            @Valid @RequestBody LoanStatementRequestDto statementDto
    );

    @Operation(
            summary = "Применение выбранного кредитного предложения",
            description = "Сохраняет кредитное предложение"
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
    ResponseEntity<Void> selectOffer(
            @Valid @RequestBody LoanOfferDto loanOfferDto
    );
}
