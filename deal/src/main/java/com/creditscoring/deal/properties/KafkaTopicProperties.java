package com.creditscoring.deal.properties;

import com.creditscoring.deal.enums.EmailTheme;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "spring.kafka.topics")
public record KafkaTopicProperties(
        Integer partitions,
        Integer replicas,
        Map<EmailTheme, String> names
) { }
