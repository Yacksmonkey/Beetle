package com.example.Beetle.repository;

import com.example.Beetle.model.UserRecommendation;
import com.example.Beetle.model.UserRecommendationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRecommendationRepository extends JpaRepository<UserRecommendation, Long> {

    Page<UserRecommendation> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<UserRecommendation> findByUserIdAndRecommendationId(Long userId, Long recommendationId);

    Page<UserRecommendation> findByUserIdAndStatusOrderByCreatedAtDesc(
            Long userId,
            UserRecommendationStatus status,
            Pageable pageable
    );

    Page<UserRecommendation> findByUserIdInAndStatusOrderByCreatedAtDesc(
            java.util.List<Long> userIds,
            UserRecommendationStatus status,
            Pageable pageable
    );
}
