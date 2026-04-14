package com.creditscoring.statement.service;

import com.creditscoring.statement.dto.request.LoanStatementRequestDto;
import com.creditscoring.statement.dto.LoanOfferDto;
import com.creditscoring.statement.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    private ApiError extractApiError(RestClientResponseException e) {
        try {
            return objectMapper.readValue(
                    e.getResponseBodyAsString(),
                    ApiError.class
            );
        } catch (Exception mappingException) {
            log.debug("Не удалось обработать ошибку вызова МС Сделка");
            return null;
        }
    }

    private<T> T execute(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (RestClientResponseException e) {
            log.debug("МС Сделка вернул ошибку: {}", e.getResponseBodyAsString());

            ApiError apiError = extractApiError(e);

            throw new ResponseStatusException(
                    e.getStatusCode(),
                    apiError != null ? apiError.message() : e.getStatusText()
            );
        }
    }

    public List<LoanOfferDto> prescoring(LoanStatementRequestDto statementDto) {
        return execute(() ->
                restClient
                        .post()
                        .uri("/statement")
                        .body(statementDto)
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<LoanOfferDto>>() {})
        );
    }

    public void selectOffer(LoanOfferDto loanOfferDto) {
        execute(() ->
                restClient
                        .post()
                        .uri("/offer/select")
                        .body(loanOfferDto)
                        .retrieve()
                        .toBodilessEntity()
        );
    }
}
