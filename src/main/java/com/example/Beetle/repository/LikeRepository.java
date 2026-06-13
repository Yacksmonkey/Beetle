package com.example.Beetle.repository;

import com.example.Beetle.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndHistoryId(Long userId, Long historyId);

    long countByHistoryId(Long historyId);
}