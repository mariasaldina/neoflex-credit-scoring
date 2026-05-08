package com.creditscoring.gateway.api;

import com.creditscoring.gateway.dto.LoanOfferDto;
import com.creditscoring.gateway.dto.request.LoanStatementRequestDto;
import com.creditscoring.gateway.exception.ApiError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "API Gateway", description = "API Gateway для работы с заявкой")
@ApiResponses({
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

    @PostMapping
    ResponseEntity<List<LoanOfferDto>> createStatement(@RequestBody LoanStatementRequestDto dto);

    @PostMapping("/select")
    void selectOffer();

    @PostMapping("/registration/{statementId}")
    void finishRegistration();
}
