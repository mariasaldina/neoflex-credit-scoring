package com.creditscoring.statement.service;

import com.creditscoring.statement.dto.LoanOfferDto;
import com.creditscoring.statement.exception.ApiError;
import com.creditscoring.statement.utils.TestDataFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@Profile("test")
public class StatementServiceTest {

    private MockRestServiceServer server;
    private StatementService statementService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    public void setUp() {
        RestClient.Builder restClientBuilder = RestClient.builder()
                .baseUrl("http://localhost:8081/deal");

        server = MockRestServiceServer.bindTo(restClientBuilder).build();

        statementService = new StatementService(restClientBuilder.build(), objectMapper);
    }

    @Test
    void prescoringTest_success() throws JsonProcessingException {
        List<LoanOfferDto> expected = List.of(TestDataFactory.createLoanOfferDto().build());

        server.expect(requestTo("http://localhost:8081/deal/statement"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(expected),
                        MediaType.APPLICATION_JSON
                ));

        List<LoanOfferDto> result = statementService.prescoring(
                TestDataFactory.createLoanStatementBuilder().build()
        );

        assertEquals(expected, result);
        server.verify();
    }

    @Test
    void selectOfferTest_success() {
        server.expect(requestTo("http://localhost:8081/deal/offer/select"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        statementService.selectOffer(
                TestDataFactory.createLoanOfferDto().build()
        );

        server.verify();
    }

    @Test
    void selectOfferTest_failure() throws JsonProcessingException {
        String message = "Заявка не найдена";
        ApiError apiError = TestDataFactory.createApiError(
                HttpStatus.NOT_FOUND,
                message,
                List.of()
        );

        server.expect(requestTo("http://localhost:8081/deal/offer/select"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(apiError))
                );

        ResponseStatusException e = assertThrows(
                ResponseStatusException.class,
                () -> statementService.selectOffer(TestDataFactory.createLoanOfferDto().build())
        );

        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        assertEquals(message, e.getReason());
    }
}
