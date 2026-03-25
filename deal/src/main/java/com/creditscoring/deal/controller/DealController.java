package com.creditscoring.deal.controller;

import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.dto.LoanOfferDto;
import com.creditscoring.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> createStatement(
            @RequestBody LoanStatementRequestDto reqBody
    ) {
        return ResponseEntity.ok(this.dealService.saveStatement(reqBody));
    }

    @PostMapping("/offer/select")
    public void selectOffer() {

    }

    @PostMapping("/calculate/{statementId}")
    public void calculateCredit() {

    }
}
