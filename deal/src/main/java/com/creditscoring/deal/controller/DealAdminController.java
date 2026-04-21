package com.creditscoring.deal.controller;

import com.creditscoring.deal.dto.request.StatusDto;
import com.creditscoring.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deal/admin/statement/{statementId}")
@RequiredArgsConstructor
public class DealAdminController {

    private final DealService dealService;

    @PutMapping("/status")
    public void updateStatus(
            @RequestBody StatusDto statusDto,
            @PathVariable UUID statementId
    ) {
        dealService.changeStatus(statementId, statusDto.status());
    }
}
