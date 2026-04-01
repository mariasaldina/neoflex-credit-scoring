package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.calculator.response.CreditDto;
import com.creditscoring.deal.dto.request.LoanOfferDto;
import com.creditscoring.deal.dto.calculator.request.ScoringDataDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CalculatorRestClient {

    private final RestClient restClient;

    private<T> T execute(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (RestClientResponseException e) {
            throw new ResponseStatusException(
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
        }
    }

    public List<LoanOfferDto> getOffers(LoanStatementRequestDto reqBody) {
        return execute(() ->
                restClient
                        .post()
                        .uri("/offers")
                        .body(reqBody)
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<LoanOfferDto>>() {})
        );
    }

    public CreditDto getCredit(ScoringDataDto reqBody) {
        return execute(() ->
                restClient
                        .post()
                        .uri("/calc")
                        .body(reqBody)
                        .retrieve()
                        .body(CreditDto.class)
        );
    }
}
