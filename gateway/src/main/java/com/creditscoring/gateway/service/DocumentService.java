package com.creditscoring.gateway.service;

import com.creditscoring.gateway.dto.request.CodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final RestClient dealClient;

    public void sendSendDocumentRequest(UUID statementId) {
        dealClient
                .post()
                .uri("/deal/document/{statementId}/send", statementId)
                .retrieve()
                .toBodilessEntity();
    }

    public void sendSignDocumentsRequest(UUID statementId) {
        dealClient
                .post()
                .uri("/deal/document/{statementId}/sign", statementId)
                .retrieve()
                .toBodilessEntity();
    }

    public void signDocuments(UUID statementId, CodeDto dto) {
        dealClient
                .post()
                .uri("/deal/document/{statementId}/code", statementId)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }
}
