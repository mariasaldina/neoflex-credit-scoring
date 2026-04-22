package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.kafka.EmailMessage;
import com.creditscoring.deal.enums.EmailTheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;
    private final EmailTextFactory emailTextFactory;

    public void send(String address, EmailTheme theme, UUID statementId) {
        send(address, theme, statementId, null);
    }

    public void send(
            String address,
            EmailTheme theme,
            UUID statementId,
            UUID sesCode
    ) {
        String text = emailTextFactory.build(theme, statementId, sesCode);
        kafkaTemplate.send(theme.getTopic(), new EmailMessage(address, theme, statementId, text));
        log.debug("Для заявки {} отправлен запрос {}", statementId, theme);
    }
}
