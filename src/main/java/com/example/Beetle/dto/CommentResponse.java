package com.example.Beetle.dto;

import java.time.OffsetDateTime;

public class CommentResponse {

    private Long id;
    private Long userId;
    private String username;
    private Long historyId;
    private String content;
    private OffsetDateTime createdAt;

    public CommentResponse() {}

    public CommentResponse(Long id, Long userId, String username, Long historyId, String content, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.historyId = historyId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}