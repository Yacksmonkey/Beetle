package com.example.Beetle.dto;

public class FriendResponse {

    private Long friendUserId;
    private String username;
    private String picture;

    public FriendResponse(Long friendUserId, String username, String picture) {
        this.friendUserId = friendUserId;
        this.username = username;
        this.picture = picture;
    }

    public Long getFriendUserId() {
        return friendUserId;
    }

    public String getUsername() {
        return username;
    }

    public String getPicture() {
        return picture;
    }
}