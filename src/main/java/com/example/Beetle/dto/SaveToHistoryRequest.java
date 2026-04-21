package com.example.Beetle.dto;


import com.example.Beetle.model.UserRecommendationStatus;

public class SaveToHistoryRequest {
    private Long recommendationId;
    private UserRecommendationStatus status; // DRAFT | SAVED | DISMISSED

    public SaveToHistoryRequest() {}

    public Long getRecommendationId() { return recommendationId; }
    public void setRecommendationId(Long recommendationId) { this.recommendationId = recommendationId; }

    public UserRecommendationStatus getStatus() { return status; }
    public void setStatus(UserRecommendationStatus status) { this.status = status; }
}
