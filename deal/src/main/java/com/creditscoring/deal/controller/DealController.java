package com.creditscoring.deal.controller;

import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.dto.LoanOfferDto;
import com.creditscoring.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> createStatement(
            @RequestBody LoanStatementRequestDto reqBody
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.dealService.saveStatement(reqBody));
    }

    @PostMapping("/offer/select")
    public ResponseEntity<?> selectOffer(
            @RequestBody LoanOfferDto reqBody
    ) {
        this.dealService.selectOffer(reqBody);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<?> calculateCredit(
            @RequestBody FinishRegistrationRequestDto reqBody,
            @PathVariable UUID statementId
    ) {
        this.dealService.calculateCredit(reqBody, statementId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
