package com.example.Beetle.controller;

import com.example.Beetle.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/request-reset")
    public String requestReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return passwordResetService.createPasswordResetToken(email);
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        return passwordResetService.resetPassword(token, newPassword);
    }
}
