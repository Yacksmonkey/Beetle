package com.example.Beetle.controller;

import com.example.Beetle.dto.CommentCreateRequest;
import com.example.Beetle.dto.CommentResponse;
import com.example.Beetle.dto.FriendRequestAcceptRequest;
import com.example.Beetle.dto.FriendRequestCreateRequest;
import com.example.Beetle.dto.FriendResponse;
import com.example.Beetle.model.Comment;
import com.example.Beetle.security.JwtUtil;
import com.example.Beetle.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;
    private final JwtUtil jwtUtil;

    public FriendController(FriendService friendService, JwtUtil jwtUtil) {
        this.friendService = friendService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/request")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> sendFriendRequest(
            @CookieValue(name = "auth_token", required = false) String token,
            @RequestBody FriendRequestCreateRequest request
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        try {
            Long senderId = jwtUtil.extractUserId(token);
            friendService.sendFriendRequest(senderId, request.getReceiverUserId());

            return ResponseEntity.ok(Map.of(
                    "message", "Friend request sent successfully"
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }

    @PostMapping("/accept")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> acceptFriendRequest(
            @CookieValue(name = "auth_token", required = false) String token,
            @RequestBody FriendRequestAcceptRequest request
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        try {
            Long receiverId = jwtUtil.extractUserId(token);
            friendService.acceptFriendRequest(receiverId, request.getRequestId());

            return ResponseEntity.ok(Map.of(
                    "message", "Friend request accepted successfully"
            ));
        } catch (RuntimeException ex) {
            if ("Not allowed".equals(ex.getMessage())) {
                return ResponseEntity.status(403).body(Map.of(
                        "message", ex.getMessage()
                ));
            }

            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getFriends(
            @CookieValue(name = "auth_token", required = false) String token
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Long userId = jwtUtil.extractUserId(token);

        var items = friendService.getFriends(userId).stream()
                .map(f -> {
                    Long friendId = f.getUserOneId().equals(userId)
                            ? f.getUserTwoId()
                            : f.getUserOneId();

                    return new FriendResponse(
                            friendId,
                            friendService.getUsernameByUserId(friendId),
                            friendService.getPictureByUserId(friendId)
                    );
                })
                .toList();

        return ResponseEntity.ok(items);
    }

    @PostMapping("/history/{id}/like")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> likeHistoryItem(
            @CookieValue(name = "auth_token", required = false) String token,
            @PathVariable Long id
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        try {
            Long userId = jwtUtil.extractUserId(token);
            boolean liked = friendService.toggleLike(userId, id);

            return ResponseEntity.ok(Map.of(
                    "liked", liked
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }

    @PostMapping("/history/{id}/comment")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> addComment(
            @CookieValue(name = "auth_token", required = false) String token,
            @PathVariable Long id,
            @RequestBody CommentCreateRequest request
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        try {
            Long userId = jwtUtil.extractUserId(token);
            Comment saved = friendService.addComment(userId, id, request.getContent());

            return ResponseEntity.ok(new CommentResponse(
                    saved.getId(),
                    saved.getUserId(),
                    friendService.getUsernameByUserId(saved.getUserId()),
                    saved.getHistoryId(),
                    saved.getContent(),
                    saved.getCreatedAt()
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }

    @GetMapping("/history/{id}/comments")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getComments(
            @CookieValue(name = "auth_token", required = false) String token,
            @PathVariable Long id
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        try {
            var items = friendService.getComments(id).stream()
                    .map(c -> new CommentResponse(
                            c.getId(),
                            c.getUserId(),
                            friendService.getUsernameByUserId(c.getUserId()),
                            c.getHistoryId(),
                            c.getContent(),
                            c.getCreatedAt()
                    ))
                    .toList();

            return ResponseEntity.ok(items);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }

    @DeleteMapping("/comments/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> deleteComment(
            @CookieValue(name = "auth_token", required = false) String token,
            @PathVariable Long id
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        try {
            Long userId = jwtUtil.extractUserId(token);
            friendService.deleteComment(userId, id);
            return ResponseEntity.ok(Map.of(
                    "message", "Comment deleted successfully"
            ));
        } catch (RuntimeException ex) {
            if ("Not allowed".equals(ex.getMessage())) {
                return ResponseEntity.status(403).body(Map.of(
                        "message", ex.getMessage()
                ));
            }

            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }
}