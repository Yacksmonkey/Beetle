package com.example.Beetle.controller;

import com.example.Beetle.dto.FeedItemResponse;
import com.example.Beetle.dto.HistoryItemResponse;
import com.example.Beetle.dto.SaveToHistoryRequest;
import com.example.Beetle.model.UserRecommendation;
import com.example.Beetle.model.UserRecommendationStatus;
import com.example.Beetle.security.JwtUtil;
import com.example.Beetle.service.FriendService;
import com.example.Beetle.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> saveToHistory(
            @CookieValue(name = "auth_token", required = false) String token,
            @RequestBody SaveToHistoryRequest request
    ) {
        if (token == null) return ResponseEntity.status(401).body("Not authenticated");

        try {
            Long userId = jwtUtil.extractUserId(token);

            UserRecommendation saved = historyService.saveToHistory(
                    userId,
                    request.getRecommendationId(),
                    request.getStatus()
            );

            return ResponseEntity.ok(Map.of(
                    "historyId", saved.getId(),
                    "status", saved.getStatus().name()
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> myHistory(
            @CookieValue(name = "auth_token", required = false) String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) UserRecommendationStatus status
    ) {
        if (token == null) return ResponseEntity.status(401).body("Not authenticated");

        Long userId = jwtUtil.extractUserId(token);

        Pageable pageable = PageRequest.of(page, size);

        var resultPage = (status == null)
                ? historyService.getMyHistory(userId, pageable)
                : historyService.getMyHistoryByStatus(userId, status, pageable);

        List<HistoryItemResponse> items = resultPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(Map.of(
                "items", items,
                "page", resultPage.getNumber(),
                "size", resultPage.getSize(),
                "totalItems", resultPage.getTotalElements(),
                "totalPages", resultPage.getTotalPages()
        ));
    }

    @GetMapping("/feed")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> friendsFeed(
            @CookieValue(name = "auth_token", required = false) String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        if (token == null) return ResponseEntity.status(401).body("Not authenticated");

        Long userId = jwtUtil.extractUserId(token);
        Pageable pageable = PageRequest.of(page, size);

        var resultPage = historyService.getFriendsFeed(userId, pageable);

        List<FeedItemResponse> items = resultPage.getContent().stream()
                .map(ur -> {
                    var rec = ur.getRecommendation();

                    return new FeedItemResponse(
                            ur.getId(),
                            ur.getUserId(),
                            friendService.getUsernameByUserId(ur.getUserId()),
                            friendService.getPictureByUserId(ur.getUserId()),
                            ur.getCreatedAt(),
                            rec.getId(),
                            rec.getType().name(),
                            rec.getTitle(),
                            rec.getDescription(),
                            rec.getImageUrl(),
                            rec.getExternalUrl()
                    );
                })
                .toList();

        return ResponseEntity.ok(Map.of(
                "items", items,
                "page", resultPage.getNumber(),
                "size", resultPage.getSize(),
                "totalItems", resultPage.getTotalElements(),
                "totalPages", resultPage.getTotalPages()
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> deleteHistoryItem(
            @CookieValue(name = "auth_token", required = false) String token,
            @PathVariable Long id
    ) {
        if (token == null) return ResponseEntity.status(401).body("Not authenticated");

        try {
            Long userId = jwtUtil.extractUserId(token);
            historyService.deleteHistoryItem(userId, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            if ("Not allowed".equals(ex.getMessage())) {
                return ResponseEntity.status(403).body(Map.of("message", ex.getMessage()));
            }
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    private HistoryItemResponse toResponse(UserRecommendation ur) {
        var rec = ur.getRecommendation();
        return new HistoryItemResponse(
                ur.getId(),
                ur.getStatus(),
                ur.getCreatedAt(),
                rec.getId(),
                rec.getType(),
                rec.getTitle(),
                rec.getDescription(),
                rec.getImageUrl(),
                rec.getExternalUrl()
        );
    }
}