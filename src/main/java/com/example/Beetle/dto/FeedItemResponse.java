package com.example.Beetle.dto;

import java.time.OffsetDateTime;

public class FeedItemResponse {

    private Long historyId;
    private Long userId;
    private String username;
    private String picture;
    private OffsetDateTime createdAt;

    private Long recommendationId;
    private String type;
    private String title;
    private String description;
    private String imageUrl;
    private String externalUrl;

    public FeedItemResponse(
            Long historyId,
            Long userId,
            String username,
            String picture,
            OffsetDateTime createdAt,
            Long recommendationId,
            String type,
            String title,
            String description,
            String imageUrl,
            String externalUrl
    ) {
        this.historyId = historyId;
        this.userId = userId;
        this.username = username;
        this.picture = picture;
        this.createdAt = createdAt;
        this.recommendationId = recommendationId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.externalUrl = externalUrl;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPicture() {
        return picture;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getRecommendationId() {
        return recommendationId;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getExternalUrl() {
        return externalUrl;
    }
}