package com.example.Beetle.service;

import com.example.Beetle.model.PasswordResetToken;
import com.example.Beetle.model.User;
import com.example.Beetle.repository.PasswordResetTokenRepository;
import com.example.Beetle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String createPasswordResetToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return "Error: User with this email not found.";
        }

        User user = userOpt.get();

        // Deleted old token
        tokenRepository.deleteByUser(user);

        // Create new token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiry);
        tokenRepository.save(resetToken);


        return "Password reset token (simulated): " + token;
    }

    // 🔹 Paso 2: Validate token and change the password
    public String resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return "Error: Invalid token.";
        }

        PasswordResetToken resetToken = tokenOpt.get();

        if (resetToken.isExpired()) {
            return "Error: Token expired.";
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Remove used token
        tokenRepository.delete(resetToken);

        return "Password successfully reset for user: " + user.getEmail();
    }
}
