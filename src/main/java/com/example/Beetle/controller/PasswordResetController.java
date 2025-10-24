package com.example.Beetle.controller;

import com.example.Beetle.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;


    @PostMapping("/request-reset")
    public String requestReset(@RequestParam String email) {
        return passwordResetService.createPasswordResetToken(email);
    }


    @PostMapping("/reset")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        return passwordResetService.resetPassword(token, newPassword);
    }
}
