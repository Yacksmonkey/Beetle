package com.example.Beetle.dto;

public class FriendRequestCreateRequest {

    private Long receiverUserId;

    public FriendRequestCreateRequest() {}

    public Long getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(Long receiverUserId) {
        this.receiverUserId = receiverUserId;
    }
}