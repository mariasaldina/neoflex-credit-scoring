package com.creditscoring.statement.controller;

import com.creditscoring.statement.dto.request.LoanStatementRequestDto;
import com.creditscoring.statement.properties.PrescoringProperties;
import com.creditscoring.statement.service.StatementService;
import com.creditscoring.statement.utils.TestDataFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatementController.class)
@EnableConfigurationProperties(PrescoringProperties.class)
public class StatementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StatementService statementService;

    @Test
    void prescoringTest_success() throws Exception {
        LoanStatementRequestDto req = TestDataFactory.createLoanStatementBuilder().build();

        mockMvc.perform(
                        post("/statement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("com.creditscoring.statement.utils.InvalidRequestFactory#loanOffers")
    void prescoringTest_failure(LoanStatementRequestDto req, String errorField)
        throws JsonProcessingException, Exception
    {
        mockMvc.perform(
                        post("/statement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Ошибка прескоринга"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.details", hasItem(startsWith(errorField + ":"))));
    }
}
