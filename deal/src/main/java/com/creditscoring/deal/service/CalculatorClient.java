package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.LoanOfferDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorClient {

    private final RestClient restClient;

    public List<LoanOfferDto> getOffers(LoanStatementRequestDto reqBody) {
        try {
            return restClient
                    .post()
                    .uri("/offers")
                    .body(reqBody)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<LoanOfferDto>>() {
                    });
        } catch (RestClientResponseException e) {
            throw new ResponseStatusException(
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
        }
    }
}
