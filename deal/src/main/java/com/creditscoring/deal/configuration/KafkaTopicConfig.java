package com.creditscoring.deal.configuration;

import com.creditscoring.deal.properties.KafkaTopicProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "kafka.enabled",
        havingValue = "true"
)
public class KafkaTopicConfig {

    private final KafkaTopicProperties properties;

    @Bean
    public KafkaAdmin.NewTopics createTopics() {
        return new KafkaAdmin.NewTopics(
                properties.names().values().stream()
                        .map(name -> TopicBuilder
                                .name(name)
                                .partitions(properties.partitions())
                                .replicas(properties.replicas())
                                .build())
                        .toArray(NewTopic[]::new)
        );
    }
}
