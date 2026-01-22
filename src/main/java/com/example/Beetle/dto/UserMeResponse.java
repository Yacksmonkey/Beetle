package com.example.Beetle.dto;

import java.util.List;

public class UserMeResponse {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String picture;
    private boolean publicProfile;

    private String phone;
    private String address;
    private String bio;

    private List<String> roles;

    public UserMeResponse(
            Long id,
            String name,
            String username,
            String email,
            String picture,
            boolean publicProfile,
            String phone,
            String address,
            String bio,
            List<String> roles
    ) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.publicProfile = publicProfile;
        this.phone = phone;
        this.address = address;
        this.bio = bio;
        this.roles = roles;
    }

    // GETTERS

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPicture() { return picture; }
    public boolean isPublicProfile() { return publicProfile; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getBio() { return bio; }
    public List<String> getRoles() { return roles; }
}
