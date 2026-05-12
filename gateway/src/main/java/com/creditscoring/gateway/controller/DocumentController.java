package com.creditscoring.gateway.controller;

import com.creditscoring.gateway.api.DocumentApi;
import com.creditscoring.gateway.dto.request.CodeDto;
import com.creditscoring.gateway.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/document/{statementId}")
@RequiredArgsConstructor
public class DocumentController implements DocumentApi {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<Void> sendCreateDocumentsRequest(
            @PathVariable UUID statementId
    ) {
        documentService.sendSendDocumentRequest(statementId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/sign")
    public ResponseEntity<Void> sendSignDocumentsRequest(
            @PathVariable UUID statementId
    ) {
        documentService.sendSignDocumentsRequest(statementId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/sign/code")
    public ResponseEntity<Void> signDocuments(
            @Valid @RequestBody CodeDto dto,
            @PathVariable UUID statementId
    ) {
        documentService.signDocuments(statementId, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
