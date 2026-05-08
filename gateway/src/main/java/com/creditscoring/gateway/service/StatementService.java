package com.creditscoring.gateway.service;

import com.creditscoring.gateway.dto.LoanOfferDto;
import com.creditscoring.gateway.dto.request.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementService {

    private final RestClient statementClient;

    public List<LoanOfferDto> createStatement(LoanStatementRequestDto dto) {
        return statementClient
                .post()
                .uri("/statement")
                .body(dto)
                .retrieve()
                .body(new ParameterizedTypeReference<List<LoanOfferDto>>() {});
    }


}
