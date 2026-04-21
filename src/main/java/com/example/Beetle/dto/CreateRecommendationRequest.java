package com.example.Beetle.dto;

import com.example.Beetle.model.RecommendationType;

public class CreateRecommendationRequest {

    private RecommendationType type;
    private String title;
    private String description;
    private String imageUrl;
    private String externalUrl;
    private String cardKey;

    public CreateRecommendationRequest() {}

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

    public String getCardKey() { return cardKey; }
    public void setCardKey(String cardKey) { this.cardKey = cardKey; }
}