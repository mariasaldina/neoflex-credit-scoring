package com.creditscoring.gateway.controller;

import com.creditscoring.gateway.api.DocumentApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document/{statementId}")
public class DocumentController implements DocumentApi {

    @PostMapping
    public void sendSendDocumentsRequest() {

    }

    @PostMapping("/sign")
    public void sendSignDocumentsRequest() {

    }

    @PostMapping("/sign/code")
    public void signDocuments() {

    }
}
