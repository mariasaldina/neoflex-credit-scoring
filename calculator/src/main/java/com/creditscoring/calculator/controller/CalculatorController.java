package com.creditscoring.calculator.controller;

import com.creditscoring.calculator.advice.GlobalExceptionHandler;
import com.creditscoring.calculator.dto.CreditDto;
import com.creditscoring.calculator.dto.LoanOfferDto;
import com.creditscoring.calculator.dto.LoanStatementRequestDto;
import com.creditscoring.calculator.dto.ScoringDataDto;
import com.creditscoring.calculator.service.CalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private final CalculatorService calculatorService;
    private final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    public CalculatorController(
            CalculatorService calculatorService
    ) {
        this.calculatorService = calculatorService;
    }

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
    public List<LoanOfferDto> createOffers(@Valid @RequestBody LoanStatementRequestDto statement) {
        logger.info("\nPOST /calculator/offers request: {}\n", statement);
        List<LoanOfferDto> res = this.calculatorService.createOffers(statement.amount(), statement.term());
        logger.info("\nPOST /calculator/offers response: {}\n", res);
        return res;
    }

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
    public CreditDto calculateCredit(@Valid @RequestBody ScoringDataDto scoringData) {
        logger.info("\nPOST /calculator/calc request: {}\n", scoringData);
        CreditDto res = this.calculatorService.calculateCredit(scoringData);
        logger.info("\nPOST /calculator/calc response: {}\n", res);
        return res;
    }
}
