package com.creditscoring.calculator.controller;

import com.creditscoring.calculator.dto.response.CreditDto;
import com.creditscoring.calculator.dto.response.LoanOfferDto;
import com.creditscoring.calculator.dto.request.LoanStatementRequestDto;
import com.creditscoring.calculator.dto.request.ScoringDataDto;
import com.creditscoring.calculator.service.CalculatorService;
import com.creditscoring.calculator.api.CalculatorApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
public class CalculatorController implements CalculatorApi {
    private final CalculatorService calculatorService;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> createOffers(@Valid @RequestBody LoanStatementRequestDto statement) {
        List<LoanOfferDto> res = this.calculatorService.createOffers(statement.amount(), statement.term());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCredit(@Valid @RequestBody ScoringDataDto scoringData) {
        CreditDto res = this.calculatorService.calculateCredit(scoringData);
        return ResponseEntity.ok(res);
    }
}
