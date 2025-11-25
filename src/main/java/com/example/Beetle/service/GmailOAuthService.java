package com.example.Beetle.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.annotation.PostConstruct;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Service
public class GmailOAuthService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${gmail.oauth.clientId}")
    private String clientId;

    @Value("${gmail.oauth.clientSecret}")
    private String clientSecret;

    @Value("${gmail.oauth.refreshToken}")
    private String refreshToken;

    private Session mailSession;

    @PostConstruct
    public void init() {
        // SMTP session for Gmail + OAuth2 (XOAUTH2)
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        mailSession = Session.getInstance(props);
    }

    // Get an accessToken using clientId, clientSecret and refreshToken
    private String getAccessToken() throws GeneralSecurityException, java.io.IOException {
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(JacksonFactory.getDefaultInstance())
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setRefreshToken(refreshToken);

        credential.refreshToken();
        return credential.getAccessToken();
    }

    // Simple HTML mail via Gmail SMTP + XOAUTH2
    public void sendEmail(String to, String subject, String htmlBody) {
        try {
            String accessToken = getAccessToken();

            Transport transport = mailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com", fromEmail, accessToken);

            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject, StandardCharsets.UTF_8.name());
            message.setText(htmlBody, StandardCharsets.UTF_8.name(), "html");

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            throw new RuntimeException("Error sending Gmail OAuth2 email: " + e.getMessage(), e);
        }
    }
}
