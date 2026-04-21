package com.example.Beetle.repository;

import com.example.Beetle.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findBySenderUserIdAndReceiverUserId(Long senderUserId, Long receiverUserId);

}