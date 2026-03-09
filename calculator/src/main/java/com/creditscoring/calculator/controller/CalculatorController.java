package com.creditscoring.calculator.controller;

import com.creditscoring.calculator.dto.LoanOfferDto;
import com.creditscoring.calculator.dto.LoanStatementRequestDto;
import com.creditscoring.calculator.service.CalculatorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private final CalculatorService calculatorService;

    public CalculatorController(
            CalculatorService calculatorService
    ) {
        this.calculatorService = calculatorService;
    }


}
