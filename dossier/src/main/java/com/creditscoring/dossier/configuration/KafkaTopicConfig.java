package com.creditscoring.dossier.configuration;

import com.creditscoring.dossier.properties.KafkaTopicProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaTopicProperties properties;

    @Bean
    public KafkaAdmin.NewTopics createTopics() {
        return new KafkaAdmin.NewTopics(
                properties.names().stream()
                        .map(name -> TopicBuilder
                                .name(name)
                                .partitions(properties.partitions())
                                .replicas(properties.replicas())
                                .build())
                        .toArray(NewTopic[]::new)
        );
    }
}
