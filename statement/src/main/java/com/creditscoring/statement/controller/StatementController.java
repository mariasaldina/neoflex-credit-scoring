package com.creditscoring.statement.controller;

import com.creditscoring.statement.api.StatementApi;
import com.creditscoring.statement.dto.request.LoanStatementRequestDto;
import com.creditscoring.statement.dto.LoanOfferDto;
import com.creditscoring.statement.service.StatementService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<LoanOfferDto>> prescoring(
            @Valid @RequestBody LoanStatementRequestDto statementDto
    ) {
        return ResponseEntity.ok(this.statementService.prescoring(statementDto));
    }

    @PostMapping("/offer")
    public ResponseEntity<Void> selectOffer(
            @Valid @RequestBody LoanOfferDto loanOfferDto
    ) {
        this.statementService.selectOffer(loanOfferDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
