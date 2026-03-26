package com.creditscoring.deal.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(Logbook logbook) {
        return RestClient.builder()
                .baseUrl("http://localhost:8080/calculator")
                .requestInterceptor(new LogbookClientHttpRequestInterceptor(logbook))
                .build();
    }
}
