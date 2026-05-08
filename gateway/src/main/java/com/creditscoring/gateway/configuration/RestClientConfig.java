package com.creditscoring.gateway.configuration;

import com.creditscoring.gateway.exception.ApiError;
import com.creditscoring.gateway.exception.ExternalApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    @Value("${deal.url}")
    private String dealUrl;

    @Value("${statement.url}")
    private String statementUrl;

    private final ObjectMapper objectMapper;

    private ApiError extractApiError(ClientHttpResponse res) {
        try {
            return objectMapper.readValue(res.getBody(), ApiError.class);
        } catch (Exception mappingException) {
            log.debug("Не удалось обработать ошибку вызова внешнего МС");
            return null;
        }
    }

    public RestClient restClient(String url, Logbook logbook) {
        return RestClient.builder()
                .baseUrl(url)
                .requestInterceptor(new LogbookClientHttpRequestInterceptor(logbook))
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (req, res) -> {
                            ApiError err = extractApiError(res);
                            log.debug("Запрос на {} вернул ошибку: {}", req.getURI(), err);
                            throw new ExternalApiException(err);
                        }
                )
                .build();
    }

    @Bean
    public RestClient dealClient(Logbook logbook) {
        return restClient(dealUrl, logbook);
    }

    @Bean
    public RestClient statementClient(Logbook logbook) {
        return restClient(statementUrl, logbook);
    }
}
