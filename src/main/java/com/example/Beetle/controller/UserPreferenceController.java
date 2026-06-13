package com.example.Beetle.controller;

import com.example.Beetle.model.UserPreference;
import com.example.Beetle.repository.UserPreferenceRepository;
import com.example.Beetle.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/preferences")
public class UserPreferenceController {

    private final UserPreferenceRepository repository;
    private final JwtUtil jwtService;

    public UserPreferenceController(UserPreferenceRepository repository, JwtUtil jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    private Long getUserIdFromToken(String token) {
        return jwtService.extractUserId(token.replace("Bearer ", ""));
    }

    @GetMapping
    public ResponseEntity<List<UserPreference>> getMyPreferences(
            @RequestHeader("Authorization") String auth
    ) {
        Long userId = getUserIdFromToken(auth);
        return ResponseEntity.ok(repository.findByUserId(userId));
    }

    @PutMapping
    public ResponseEntity<?> updatePreferences(
            @RequestHeader("Authorization") String auth,
            @RequestBody List<UserPreference> preferences
    ) {
        Long userId = getUserIdFromToken(auth);

        // delete existing prefs
        repository.deleteByUserId(userId);

        long now = System.currentTimeMillis();

        // assign correct userId to all new preferences
        preferences.forEach(p -> {
            p.setId(null);
            p.setUserId(userId);
            p.setUpdatedAt(now);
        });

        repository.saveAll(preferences);

        return ResponseEntity.ok("Preferences updated");
    }
}
