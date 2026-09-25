package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendVerificationEmail(String recipient, String managerName, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(recipient);
        message.setSubject("Verify your TeamSync account");
        message.setText("""
                Hello %s,

                Click the link below to verify your TeamSync account:
                %s/verify-email?token=%s

                This link expires in 24 hours.
                """.formatted(managerName, frontendUrl, token));

        mailSender.send(message);
    }
}
