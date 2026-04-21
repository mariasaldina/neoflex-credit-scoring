package com.creditscoring.deal.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    @Value("${calculator.url}")
    private String url;

    @Bean
    public RestClient restClient(Logbook logbook) {
        return RestClient.builder()
                .baseUrl(url)
                .requestInterceptor(new LogbookClientHttpRequestInterceptor(logbook))
                .build();
    }
}
