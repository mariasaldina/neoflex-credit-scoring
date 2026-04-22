package com.creditscoring.dossier.service;

import com.creditscoring.dossier.dto.StatusDto;
import com.creditscoring.dossier.enums.ApplicationStatus;
import com.creditscoring.dossier.enums.ChangeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealRestClient {

    private final RestClient restClient;

    public void setStatusDocumentsCreated(UUID statementId) {
        restClient
                .put()
                .uri("/admin/statement/%s/status".formatted(statementId))
                .body(new StatusDto(ApplicationStatus.DOCUMENTS_CREATED, ChangeType.AUTOMATIC))
                .retrieve()
                .toBodilessEntity();
        log.debug("Отправлен запрос на изменение статуса заявки (DOCUMENTS_CREATED)");
    }
}
