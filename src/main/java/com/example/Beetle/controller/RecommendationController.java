package com.example.Beetle.controller;

import com.example.Beetle.dto.CreateRecommendationRequest;
import com.example.Beetle.model.Recommendation;
import com.example.Beetle.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> create(@RequestBody CreateRecommendationRequest request) {
        try {
            Recommendation created = recommendationService.create(request);
            return ResponseEntity.ok(Map.of(
                    "id", created.getId()
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }

    @GetMapping("/final")
    public ResponseEntity<?> getFinalRecommendations(@RequestParam String cardKey) {
        try {
            return ResponseEntity.ok(recommendationService.getByCardKey(cardKey));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }
}