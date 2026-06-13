package com.example.Beetle.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "user_recommendations",
        uniqueConstraints = @UniqueConstraint(name = "ux_user_reco_user_reco", columnNames = {"user_id", "recommendation_id"})
)

public class UserRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We map only the user id to avoid touching User entity with relationships.
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recommendation_id", nullable = false)
    private Recommendation recommendation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRecommendationStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public UserRecommendation() {}

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Recommendation getRecommendation() { return recommendation; }
    public void setRecommendation(Recommendation recommendation) { this.recommendation = recommendation; }

    public UserRecommendationStatus getStatus() { return status; }
    public void setStatus(UserRecommendationStatus status) { this.status = status; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
