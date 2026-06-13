package com.example.Beetle.controller;

import com.example.Beetle.model.Comment;
import com.example.Beetle.repository.CommentRepository;
import com.example.Beetle.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Beetle.service.FriendService;
import java.util.List;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> createComment(
            @CookieValue(name = "auth_token", required = false) String token,
            @RequestBody Map<String, String> body
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Long userId;
        try {
            userId = jwtUtil.extractUserId(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        Long historyId = Long.valueOf(body.get("historyId"));
        String content = body.get("content");

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setHistoryId(historyId);
        comment.setContent(content);
        comment.setCreatedAt(OffsetDateTime.now());

        commentRepository.save(comment);

        return ResponseEntity.ok(Map.of(
                "message", "Comment created"
        ));
    }

    @Autowired
    private FriendService friendService;

    @GetMapping("/{historyId}")
    public ResponseEntity<?> getCommentsByHistoryId(@PathVariable Long historyId) {

        List<Comment> comments = commentRepository.findByHistoryIdOrderByCreatedAtAsc(historyId);

        List<Map<String, Object>> response = comments.stream().map(comment -> {
            Map<String, Object> item = new java.util.HashMap<>();
            item.put("id", comment.getId());
            item.put("userId", comment.getUserId());
            item.put("username", friendService.getUsernameByUserId(comment.getUserId()));
            item.put("historyId", comment.getHistoryId());
            item.put("content", comment.getContent());
            item.put("createdAt", comment.getCreatedAt());
            return item;
        }).toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @CookieValue(name = "auth_token", required = false) String token,
            @PathVariable Long commentId
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Long userId;
        try {
            userId = jwtUtil.extractUserId(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body(Map.of("message", "Not allowed"));
        }

        commentRepository.delete(comment);

        return ResponseEntity.ok(Map.of(
                "message", "Comment deleted"
        ));
    }
}