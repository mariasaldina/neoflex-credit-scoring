package com.creditscoring.calculator.controller;

import com.creditscoring.calculator.dto.CreditDto;
import com.creditscoring.calculator.dto.LoanOfferDto;
import com.creditscoring.calculator.dto.LoanStatementRequestDto;
import com.creditscoring.calculator.dto.ScoringDataDto;
import com.creditscoring.calculator.service.CalculatorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private final CalculatorService calculatorService;
    private final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    public CalculatorController(
            CalculatorService calculatorService
    ) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/offers")
    public List<LoanOfferDto> createOffers(@Valid @RequestBody LoanStatementRequestDto statement) {
        logger.info("\nPOST /calculator/offers request: {}\n", statement);
        List<LoanOfferDto> res = this.calculatorService.createOffers(statement.amount(), statement.term());
        logger.info("\nPOST /calculator/offers response: {}\n", res);
        return res;
    }

    @PostMapping("/calc")
    public CreditDto calculateCredit(@Valid @RequestBody ScoringDataDto scoringData) {
        logger.info("\nPOST /calculator/calc request: {}\n", scoringData);
        CreditDto res = this.calculatorService.calculateCredit(scoringData);
        logger.info("\nPOST /calculator/calc response: {}\n", res);
        return res;
    }
}
