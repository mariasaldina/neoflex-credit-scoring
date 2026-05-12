package com.creditscoring.gateway.controller;

import com.creditscoring.gateway.api.StatementApi;
import com.creditscoring.gateway.dto.LoanOfferDto;
import com.creditscoring.gateway.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.gateway.dto.request.LoanStatementRequestDto;
import com.creditscoring.gateway.service.StatementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class StatementController implements StatementApi {

    private final StatementService statementService;

    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> createStatement(
            @Valid @RequestBody LoanStatementRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(statementService.createStatement(dto));
    }

    @PostMapping("/select")
    public ResponseEntity<Void> selectOffer(
            @Valid @RequestBody LoanOfferDto dto
    ) {
        statementService.selectOffer(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/registration/{statementId}")
    public ResponseEntity<Void> finishRegistration(
            @Valid @RequestBody FinishRegistrationRequestDto dto,
            @PathVariable UUID statementId
    ) {
        statementService.finishRegistration(dto, statementId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
