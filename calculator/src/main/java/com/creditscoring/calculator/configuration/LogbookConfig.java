package com.creditscoring.calculator.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Precorrelation;

@Configuration
public class LogbookConfig {

    @Bean
    public HttpLogWriter infoHttpLogWriter() {
        return new InfoLevelHttpLogWriter();
    }

    @Slf4j(topic = "org.zalando.logbook.Logbook")
    private static class InfoLevelHttpLogWriter implements HttpLogWriter {
        @Override
        public void write(Precorrelation precorrelation, String request) {
            log.info(request);
        }

        @Override
        public void write(Correlation correlation, String response) {
            log.info(response);
        }
    }
}
