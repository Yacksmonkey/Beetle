package com.example.Beetle.service;

import com.example.Beetle.model.Recommendation;
import com.example.Beetle.model.UserRecommendation;
import com.example.Beetle.model.UserRecommendationStatus;
import com.example.Beetle.repository.RecommendationRepository;
import com.example.Beetle.repository.UserRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.Beetle.repository.FriendshipRepository;

import java.time.OffsetDateTime;


@Service
public class HistoryService {

    @Autowired
    private UserRecommendationRepository userRecommendationRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    /**
     * Saves a recommendation into user's history (draft/saved/dismissed).
     * No duplicates: if the user already has it, we update the status instead.
     */
    public UserRecommendation saveToHistory(Long userId, Long recommendationId, UserRecommendationStatus status) {

        if (userId == null) throw new RuntimeException("UserId is required");
        if (recommendationId == null) throw new RuntimeException("RecommendationId is required");
        if (status == null) throw new RuntimeException("Status is required");

        Recommendation rec = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));

        return userRecommendationRepository.findByUserIdAndRecommendationId(userId, recommendationId)
                .map(existing -> {
                    // Update status only (keeps original createdAt as "first time saved")
                    existing.setStatus(status);
                    return userRecommendationRepository.save(existing);
                })
                .orElseGet(() -> {
                    UserRecommendation ur = new UserRecommendation();
                    ur.setUserId(userId);
                    ur.setRecommendation(rec);
                    ur.setStatus(status);
                    ur.setCreatedAt(OffsetDateTime.now());
                    return userRecommendationRepository.save(ur);
                });
    }

    public Page<UserRecommendation> getMyHistory(Long userId, Pageable pageable) {
        if (userId == null) throw new RuntimeException("UserId is required");
        return userRecommendationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<UserRecommendation> getMyHistoryByStatus(Long userId, UserRecommendationStatus status, Pageable pageable) {
        if (userId == null) throw new RuntimeException("UserId is required");
        if (status == null) throw new RuntimeException("Status is required");
        return userRecommendationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status, pageable);
    }

    /**
     * Deletes a history row (does NOT delete the global recommendation).
     * Ownership check is mandatory.
     */
    public void deleteHistoryItem(Long userId, Long historyId) {
        if (userId == null) throw new RuntimeException("UserId is required");
        if (historyId == null) throw new RuntimeException("HistoryId is required");

        UserRecommendation ur = userRecommendationRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("History item not found"));

        if (!userId.equals(ur.getUserId()))
            throw new RuntimeException("Not allowed");

        userRecommendationRepository.deleteById(historyId);
    }

    @Autowired
    private FriendshipRepository friendshipRepository;

    public Page<UserRecommendation> getFriendsFeed(Long userId, Pageable pageable) {
        if (userId == null) throw new RuntimeException("UserId is required");

        var friendships = friendshipRepository.findByUserOneIdOrUserTwoId(userId, userId);

        java.util.List<Long> friendIds = friendships.stream()
                .map(f -> f.getUserOneId().equals(userId) ? f.getUserTwoId() : f.getUserOneId())
                .distinct()
                .toList();

        if (friendIds.isEmpty()) {
            return Page.empty(pageable);
        }

        return userRecommendationRepository.findByUserIdInAndStatusOrderByCreatedAtDesc(
                friendIds,
                UserRecommendationStatus.SAVED,
                pageable
        );
    }
}

