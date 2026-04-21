package com.creditscoring.deal.service;

import com.creditscoring.deal.enums.EmailTheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailTextFactory {

    @Value("${email.base-url}")
    private String baseUrl;

    public String build(EmailTheme theme, UUID statementId, UUID sesCode) {
        return switch (theme) {
            case FINISH_REGISTRATION ->
                    "Ваша заявка предварительно одобрена, завершите оформление";
            case CREATE_DOCUMENTS ->
                    """
                    Перейдите по ссылке, чтобы сформировать документы для оформления кредита:
                    %s/deal/document/%s/send
                    """.formatted(baseUrl, statementId);
            case SEND_DOCUMENTS ->
                    """
                    Вы получили документы для подписания.
                    Перейдите по ссылке, чтобы подтвердить согласие с условиями:
                    %s/deal/document/%s/sign
                    """.formatted(baseUrl, statementId);
            case SEND_SES -> {
                    if (sesCode == null) {
                        throw new IllegalArgumentException(
                                "sesCode обязателен для email типа SEND_SES"
                        );
                    }
                    yield
                            """
                            Ваш код для подписания документов, никому не сообщайте его: %s
                            Для подписания перейдите по ссылке и укажите код:
                            %s/deal/document/%s/code
                            """.formatted(sesCode, baseUrl, statementId);
            }
            case CREDIT_ISSUED ->
                    "Кредит по вашей заявке успешно оформлен";
            case STATEMENT_DENIED ->
                    "Ваша заявка не прошла скоринг и была отклонена";
            default -> "";
        };
    }
}
