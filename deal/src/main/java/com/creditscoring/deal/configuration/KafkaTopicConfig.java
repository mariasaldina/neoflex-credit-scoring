package com.creditscoring.deal.configuration;

import com.creditscoring.deal.enums.EmailTheme;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class KafkaTopicConfig {

    @Bean
    public KafkaAdmin.NewTopics createTopics() {
        return new KafkaAdmin.NewTopics(
                Arrays.stream(EmailTheme.values())
                        .map(theme -> TopicBuilder
                                .name(theme.getTopic())
                                .partitions(3)
                                .replicas(1)
                                .build())
                        .toArray(NewTopic[]::new)
        );
    }
}
