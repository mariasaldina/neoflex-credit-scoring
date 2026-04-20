package com.creditscoring.dossier.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "spring.kafka.topics")
public record KafkaTopicProperties(
        Integer partitions,
        Integer replicas,
        List<String> names
) { }
