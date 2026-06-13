package com.example.Beetle.repository;

import com.example.Beetle.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByUserOneIdAndUserTwoId(Long userOneId, Long userTwoId);

    List<Friendship> findByUserOneIdOrUserTwoId(Long userOneId, Long userTwoId);
}