package com.creditscoring.deal.controller;

import com.creditscoring.deal.api.DealAdminApi;
import com.creditscoring.deal.dto.request.StatusDto;
import com.creditscoring.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deal/admin/statement/{statementId}")
@RequiredArgsConstructor
public class DealAdminController implements DealAdminApi {

    private final DealService dealService;

    @PutMapping("/status")
    public ResponseEntity<Void> updateStatus(
            @RequestBody StatusDto statusDto,
            @PathVariable UUID statementId
    ) {
        dealService.changeStatus(statementId, statusDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
