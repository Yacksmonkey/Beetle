package com.example.Beetle.controller;

import com.example.Beetle.model.User;
import com.example.Beetle.repository.UserRepository;
import com.example.Beetle.security.JwtUtil;
import com.example.Beetle.service.SupabaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_TYPES =
            Set.of("image/png", "image/jpeg", "image/webp");

    @Autowired
    private SupabaseStorageService storageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(
            value = "/profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadProfilePicture(
            @RequestPart("file") MultipartFile file,
            HttpServletRequest request
    ) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        if (file.getSize() > MAX_SIZE) {
            return ResponseEntity.badRequest().body("File too large (max 5MB)");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            return ResponseEntity.badRequest().body("Invalid file type");
        }

        // Extract JWT from cookie
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("auth_token".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Long userId = jwtUtil.extractUserId(token);
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        try {
            String publicUrl = storageService.uploadAvatar(
                    file.getBytes(),
                    file.getContentType(),
                    userId
            );

            user.setPicture(publicUrl);
            userRepository.save(user);

            return ResponseEntity.ok(
                    java.util.Map.of("url", publicUrl)
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Upload failed");
        }
    }
}
