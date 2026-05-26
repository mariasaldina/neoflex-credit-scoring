package com.creditscoring.gateway.service;

import com.creditscoring.gateway.dto.LoanOfferDto;
import com.creditscoring.gateway.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.gateway.dto.request.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final RestClient statementClient;
    private final RestClient dealClient;

    public List<LoanOfferDto> createStatement(LoanStatementRequestDto dto) {
        return statementClient
                .post()
                .uri("")
                .body(dto)
                .retrieve()
                .body(new ParameterizedTypeReference<List<LoanOfferDto>>() {});
    }

    public void selectOffer(LoanOfferDto dto) {
        statementClient
                .post()
                .uri("/offer")
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public void finishRegistration(FinishRegistrationRequestDto dto, UUID statementId) {
        dealClient
                .post()
                .uri("/calculate/{statementId}", statementId)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }
}
