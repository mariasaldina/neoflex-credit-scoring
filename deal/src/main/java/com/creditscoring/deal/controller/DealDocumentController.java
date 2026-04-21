package com.creditscoring.deal.controller;

import com.creditscoring.deal.api.DealDocumentApi;
import com.creditscoring.deal.dto.request.CodeDto;
import com.creditscoring.deal.service.DealDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deal/document/{statementId}")
@RequiredArgsConstructor
public class DealDocumentController implements DealDocumentApi {

    private final DealDocumentService dealDocumentService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendDocumentRequest(
            @PathVariable UUID statementId
    ) {
        dealDocumentService.sendDocuments(statementId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/sign")
    public ResponseEntity<Void> signDocumentRequest(
            @PathVariable UUID statementId
    ) {
        dealDocumentService.signDocuments(statementId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/code")
    public ResponseEntity<Void> verifySesCode(
            @RequestBody CodeDto codeDto,
            @PathVariable UUID statementId
    ) {
        dealDocumentService.verifyCode(statementId, codeDto.sesCode());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
