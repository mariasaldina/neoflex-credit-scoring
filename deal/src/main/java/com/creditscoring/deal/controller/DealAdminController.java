package com.creditscoring.deal.controller;

import com.creditscoring.deal.api.DealAdminApi;
import com.creditscoring.deal.dto.request.StatusDto;
import com.creditscoring.deal.dto.response.StatementDto;
import com.creditscoring.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal/admin/statement")
@RequiredArgsConstructor
public class DealAdminController implements DealAdminApi {

    private final DealService dealService;

    @PutMapping("/{statementId}/status")
    public ResponseEntity<Void> updateStatus(
            @RequestBody StatusDto statusDto,
            @PathVariable UUID statementId
    ) {
        dealService.changeStatus(statementId, statusDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{statementId}")
    public ResponseEntity<StatementDto> getStatement(
            @PathVariable UUID statementId
    ) {
        return ResponseEntity.ok(dealService.getStatement(statementId));
    }

    @GetMapping
    public ResponseEntity<List<StatementDto>> getAllStatements() {
        return ResponseEntity.ok(dealService.getAllStatements());
    }
}
