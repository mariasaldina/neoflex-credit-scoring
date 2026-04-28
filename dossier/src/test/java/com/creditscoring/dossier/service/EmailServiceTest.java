package com.creditscoring.dossier.service;

import com.creditscoring.dossier.dto.EmailMessage;
import com.creditscoring.dossier.enums.EmailTheme;
import com.creditscoring.dossier.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender sender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendEmail_success() {
        EmailMessage message = TestDataFactory.createEmailMessage().build();
        emailService.sendEmail(message);

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(sender).send(captor.capture());

        SimpleMailMessage mail = captor.getValue();

        assertArrayEquals(new String[]{message.address()}, mail.getTo());
        assertEquals(message.theme().getSubject(), mail.getSubject());
        assertEquals(message.text(), mail.getText());
    }
}
