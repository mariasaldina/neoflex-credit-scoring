package com.creditscoring.calculator.controller;

import com.creditscoring.calculator.dto.request.ScoringDataDto;
import com.creditscoring.calculator.utils.TestDataFactory;
import com.creditscoring.calculator.properties.PrescoringProperties;
import com.creditscoring.calculator.dto.request.LoanStatementRequestDto;
import com.creditscoring.calculator.service.CalculatorService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorController.class)
@EnableConfigurationProperties(PrescoringProperties.class)
public class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CalculatorService calculatorService;

    @Test
    void calculateOffersTest_success() throws Exception {
        LoanStatementRequestDto req = TestDataFactory.createLoanStatementBuilder().build();

        mockMvc.perform(
                post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("com.creditscoring.calculator.utils.InvalidRequestFactory#loanOffers")
    void calculateOffersTest_failure(LoanStatementRequestDto req, String errorField)
            throws Exception
    {
        mockMvc.perform(
                post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Ошибка прескоринга"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.details", hasItem(startsWith(errorField + ":"))));
    }

    @Test
    void calculateCreditTest_success() throws Exception {
        ScoringDataDto req = TestDataFactory.createScoringDataBuilder().build();

        mockMvc.perform(
                        post("/calculator/calc")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("com.creditscoring.calculator.utils.InvalidRequestFactory#scoringData")
    void calculateCreditTest_failure(ScoringDataDto req, String errorField)
            throws Exception
    {
        mockMvc.perform(
                        post("/calculator/calc")
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
