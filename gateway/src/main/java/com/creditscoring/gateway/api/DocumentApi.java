package com.creditscoring.gateway.api;

import com.creditscoring.gateway.exception.ApiError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "API Gateway", description = "API Gateway для работы с документами")
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
public interface DocumentApi {

    @PostMapping("/document/{statementId}")
    void sendSendDocumentsRequest();

    @PostMapping("/document/{statementId}/sign")
    void sendSignDocumentsRequest();

    @PostMapping("/document/{statementId}/sign/code")
    void signDocuments();
}
