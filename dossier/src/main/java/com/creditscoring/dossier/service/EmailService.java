package com.creditscoring.dossier.service;

import com.creditscoring.dossier.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender sender;

    public void sendEmail(EmailMessage message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(message.address());
        simpleMailMessage.setSubject(message.theme().getSubject());
        simpleMailMessage.setText(message.text());
        sender.send(simpleMailMessage);
    }
}
