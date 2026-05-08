package com.creditscoring.gateway.controller;

import com.creditscoring.gateway.api.StatementApi;
import com.creditscoring.gateway.dto.LoanOfferDto;
import com.creditscoring.gateway.dto.request.LoanStatementRequestDto;
import com.creditscoring.gateway.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class StatementController implements StatementApi {

    private final StatementService statementService;

    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> createStatement(
            @RequestBody LoanStatementRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(statementService.createStatement(dto));
    }

    @PostMapping("/select")
    public void selectOffer() {

    }

    @PostMapping("/registration/{statementId}")
    public void finishRegistration() {

    }
}
