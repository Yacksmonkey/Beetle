package com.example.Beetle.dto;

import com.example.Beetle.model.RecommendationType;
import com.example.Beetle.model.UserRecommendationStatus;

import java.time.OffsetDateTime;

public class HistoryItemResponse {

    private Long historyId;
    private UserRecommendationStatus status;
    private OffsetDateTime savedAt;

    private Long recommendationId;
    private RecommendationType type;
    private String title;
    private String description;
    private String imageUrl;
    private String externalUrl;

    public HistoryItemResponse() {}

    public HistoryItemResponse(
            Long historyId,
            UserRecommendationStatus status,
            OffsetDateTime savedAt,
            Long recommendationId,
            RecommendationType type,
            String title,
            String description,
            String imageUrl,
            String externalUrl
    ) {
        this.historyId = historyId;
        this.status = status;
        this.savedAt = savedAt;
        this.recommendationId = recommendationId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.externalUrl = externalUrl;
    }

    public Long getHistoryId() { return historyId; }
    public void setHistoryId(Long historyId) { this.historyId = historyId; }

    public UserRecommendationStatus getStatus() { return status; }
    public void setStatus(UserRecommendationStatus status) { this.status = status; }

    public OffsetDateTime getSavedAt() { return savedAt; }
    public void setSavedAt(OffsetDateTime savedAt) { this.savedAt = savedAt; }

    public Long getRecommendationId() { return recommendationId; }
    public void setRecommendationId(Long recommendationId) { this.recommendationId = recommendationId; }

    public RecommendationType getType() { return type; }
    public void setType(RecommendationType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getExternalUrl() { return externalUrl; }
    public void setExternalUrl(String externalUrl) { this.externalUrl = externalUrl; }

}
