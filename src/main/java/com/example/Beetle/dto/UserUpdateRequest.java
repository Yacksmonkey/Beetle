package com.example.Beetle.dto;

public class UserUpdateRequest {

    private String name;
    private String username;
    private String picture;
    private Boolean publicProfile;

    private String phone;
    private String address;
    private String bio;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public Boolean getPublicProfile() { return publicProfile; }
    public void setPublicProfile(Boolean publicProfile) { this.publicProfile = publicProfile; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}




