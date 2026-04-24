package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.kafka.EmailMessage;
import com.creditscoring.deal.enums.EmailTheme;
import com.creditscoring.deal.properties.KafkaTopicProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, EmailMessage> kafkaTemplate;

    @Mock
    private EmailTextFactory emailTextFactory;

    private KafkaProducerService kafkaProducerService;
    private KafkaTopicProperties kafkaTopicProperties;

    @BeforeEach
    void setupProperties() {
        kafkaTopicProperties = new KafkaTopicProperties(
                3,
                1,
                Map.of(
                        EmailTheme.FINISH_REGISTRATION, "finish-registration"
                )
        );
        kafkaProducerService = new KafkaProducerService(
                kafkaTemplate,
                kafkaTopicProperties,
                emailTextFactory
        );
    }

    @Test
    void send_success() {
        EmailTheme theme = EmailTheme.FINISH_REGISTRATION;
        String email = "example@mail.com";
        UUID statementId = UUID.randomUUID();
        String text = "some text";
        String topic = kafkaTopicProperties.names().get(theme);

        when(emailTextFactory.build(theme, statementId, null))
                .thenReturn(text);

        kafkaProducerService.send(email, theme, statementId);

        verify(kafkaTemplate).send(
                topic,
                new EmailMessage(email, theme, statementId, text)
        );
    }
}
