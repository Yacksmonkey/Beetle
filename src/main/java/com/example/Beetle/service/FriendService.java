package com.example.Beetle.service;

import com.example.Beetle.model.*;
import com.example.Beetle.repository.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRecommendationRepository userRecommendationRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRequestRepository friendRequestRepository,
                         FriendshipRepository friendshipRepository,
                         LikeRepository likeRepository,
                         CommentRepository commentRepository,
                         UserRecommendationRepository userRecommendationRepository,
                         UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.userRecommendationRepository = userRecommendationRepository;
        this.userRepository = userRepository;
    }

    public void sendFriendRequest(Long senderId, Long receiverId) {

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("Cannot send request to yourself");
        }

        Long u1 = Math.min(senderId, receiverId);
        Long u2 = Math.max(senderId, receiverId);

        Optional<Friendship> existingFriendship =
                friendshipRepository.findByUserOneIdAndUserTwoId(u1, u2);

        if (existingFriendship.isPresent()) {
            throw new RuntimeException("Already friends");
        }

        Optional<FriendRequest> existingRequest =
                friendRequestRepository.findBySenderUserIdAndReceiverUserId(senderId, receiverId);

        if (existingRequest.isPresent()) {
            throw new RuntimeException("Request already sent");
        }

        FriendRequest fr = new FriendRequest();
        fr.setSenderUserId(senderId);
        fr.setReceiverUserId(receiverId);
        fr.setCreatedAt(OffsetDateTime.now());

        friendRequestRepository.save(fr);
    }

    public void acceptFriendRequest(Long receiverId, Long requestId) {

        if (receiverId == null) {
            throw new RuntimeException("ReceiverId is required");
        }

        if (requestId == null) {
            throw new RuntimeException("RequestId is required");
        }

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (!receiverId.equals(request.getReceiverUserId())) {
            throw new RuntimeException("Not allowed");
        }

        Long senderId = request.getSenderUserId();

        Long u1 = Math.min(senderId, receiverId);
        Long u2 = Math.max(senderId, receiverId);

        Optional<Friendship> existingFriendship =
                friendshipRepository.findByUserOneIdAndUserTwoId(u1, u2);

        if (existingFriendship.isPresent()) {
            friendRequestRepository.deleteById(requestId);
            throw new RuntimeException("Already friends");
        }

        Friendship friendship = new Friendship();
        friendship.setUserOneId(u1);
        friendship.setUserTwoId(u2);
        friendship.setCreatedAt(OffsetDateTime.now());

        friendshipRepository.save(friendship);
        friendRequestRepository.deleteById(requestId);
    }

    public List<Friendship> getFriends(Long userId) {
        if (userId == null) {
            throw new RuntimeException("UserId is required");
        }
        return friendshipRepository.findByUserOneIdOrUserTwoId(userId, userId);
    }

    public boolean toggleLike(Long userId, Long historyId) {
        if (userId == null) throw new RuntimeException("UserId is required");
        if (historyId == null) throw new RuntimeException("HistoryId is required");

        userRecommendationRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("History item not found"));

        var existing = likeRepository.findByUserIdAndHistoryId(userId, historyId);

        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return false; // unlike
        }

        Like like = new Like();
        like.setUserId(userId);
        like.setHistoryId(historyId);
        like.setCreatedAt(OffsetDateTime.now());

        likeRepository.save(like);
        return true; // like
    }

    public Comment addComment(Long userId, Long historyId, String content) {
        if (userId == null) throw new RuntimeException("UserId is required");
        if (historyId == null) throw new RuntimeException("HistoryId is required");
        if (content == null || content.isBlank()) throw new RuntimeException("Content is required");

        userRecommendationRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("History item not found"));

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setHistoryId(historyId);
        comment.setContent(content.trim());
        comment.setCreatedAt(OffsetDateTime.now());

        return commentRepository.save(comment);
    }

    public List<Comment> getComments(Long historyId) {
        if (historyId == null) throw new RuntimeException("HistoryId is required");

        userRecommendationRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("History item not found"));

        return commentRepository.findByHistoryIdOrderByCreatedAtAsc(historyId);
    }

    public String getUsernameByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(User::getUsername)
                .orElse("unknown");
    }

    public String getPictureByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(User::getPicture)
                .orElse(null);
    }

    public void deleteComment(Long userId, Long commentId) {
        if (userId == null) throw new RuntimeException("UserId is required");
        if (commentId == null) throw new RuntimeException("CommentId is required");

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!userId.equals(comment.getUserId())) {
            throw new RuntimeException("Not allowed");
        }

        commentRepository.deleteById(commentId);
    }
}