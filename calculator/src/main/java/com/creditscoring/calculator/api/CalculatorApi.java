package com.creditscoring.calculator.api;

import com.creditscoring.calculator.advice.GlobalExceptionHandler;
import com.creditscoring.calculator.dto.response.CreditDto;
import com.creditscoring.calculator.dto.response.LoanOfferDto;
import com.creditscoring.calculator.dto.request.LoanStatementRequestDto;
import com.creditscoring.calculator.dto.request.ScoringDataDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Кредитный калькулятор", description = "API для скоринга и расчёта кредитных предложений")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Ошибка прескоринга / валидации",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка сервера",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)
                )
        )
})
@RequestMapping("/calculator")
public interface CalculatorApi {

    @Operation(
            summary = "Прескоринг и расчёт кредитных предложений",
            description = "Возвращает 4 предложения со всеми комбинациями страховки и бонуса для зарплатного клиента"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Заявка прошла прескоринг и предложения были успешно рассчитаны",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class))
                    )
            )
    })
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> createOffers(@Valid @RequestBody LoanStatementRequestDto statement);

    @Operation(
            summary = "Скоринг и расчёт полных условий кредита",
            description = "Возвращает итоговую ставку, полную стоимость кредита," +
                    "ежемесячный платеж и график платежей"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Заявка прошла скоринг и параметры кредита были успешно рассчитаны",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanOfferDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Ошибка скоринга",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)
                    )
            )
    })
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCredit(@Valid @RequestBody ScoringDataDto scoringData);
}
