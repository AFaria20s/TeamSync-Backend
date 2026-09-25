package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestClient restClient = RestClient.create();

    @Value("${resend.api-key:}")
    private String apiKey;

    @Value("${resend.from:}")
    private String sender;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    public void sendVerificationEmail(String recipient, String managerName, String token) {
        if (apiKey.isBlank() || sender.isBlank()) {
            throw new IllegalStateException("Resend email service is not configured");
        }

        String text = """
                Hello %s,

                Click the link below to verify your TeamSync account:
                %s/verify-email?token=%s

                This link expires in 24 hours.
                """.formatted(managerName, frontendUrl, token);

        try {
            restClient
                    .post()
                    .uri("https://api.resend.com/emails")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "from", sender,
                            "to", List.of(recipient),
                            "subject", "Verify your TeamSync account",
                            "text", text
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException exception) {
            throw new IllegalStateException("Unable to send verification email through Resend", exception);
        }
    }
}
