package com.example.Beetle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final GmailOAuthService gmailOAuthService;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendBaseUrl;

    public EmailService(GmailOAuthService gmailOAuthService) {
        this.gmailOAuthService = gmailOAuthService;
    }

    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = frontendBaseUrl + "/reset-password?token=" + token;

        String subject = "Beetle - Password Reset Request";
        String html = """
            <p>Hello,</p>
            <p>We received a request to reset your password for your Beetle account.</p>
            <p>Click the link below to reset it:</p>
            <p><a href="%s">%s</a></p>
            <p>If you did not request this, you can safely ignore this message.</p>
            """.formatted(resetUrl, resetUrl);

        gmailOAuthService.sendEmail(to, subject, html);
    }
}
