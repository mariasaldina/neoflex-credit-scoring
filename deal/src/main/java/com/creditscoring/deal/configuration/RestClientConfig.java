package com.creditscoring.deal.configuration;

import com.creditscoring.deal.properties.CalculatorServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final CalculatorServiceProperties calculatorServiceProperties;

    @Bean
    public RestClient restClient(Logbook logbook) {
        return RestClient.builder()
                .baseUrl(calculatorServiceProperties.url())
                .requestInterceptor(new LogbookClientHttpRequestInterceptor(logbook))
                .build();
    }
}
