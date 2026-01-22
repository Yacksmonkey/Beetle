package com.example.Beetle.service;

import com.example.Beetle.model.Role;
import com.example.Beetle.model.User;
import com.example.Beetle.repository.RoleRepository;
import com.example.Beetle.repository.UserRepository;

import com.example.Beetle.dto.UserUpdateRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    // REGISTER USER ----------------------------------------------------
    public User registerUser(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new RuntimeException("Email is required");

        if (user.getPassword() == null || user.getPassword().isBlank())
            throw new RuntimeException("Password is required");

        // If username is not provided, generate it from email
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            String baseUsername = user.getEmail().split("@")[0];
            String username = baseUsername;
            int counter = 1;

            while (userRepository.existsByUsername(username)) {
                username = baseUsername + counter;
                counter++;
            }

            user.setUsername(username);
        }

        // Ensure roles list is initialized
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());

        }

        if (userRepository.existsByEmail(user.getEmail()))
            throw new RuntimeException("Email already registered");

        if (userRepository.existsByUsername(user.getUsername()))
            throw new RuntimeException("Username already taken");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        List<String> adminEmails = List.of("yacksmonkey@gmail.com");
        List<String> adminUsernames = List.of("yacksmonkey");

        if (adminEmails.contains(user.getEmail()) ||
                adminUsernames.contains(user.getUsername())) {
            user.getRoles().add(adminRole);
        } else {
            user.getRoles().add(userRole);
        }

        return userRepository.save(user);
    }



    // LOGIN ----------------------------------------------------
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User authenticate(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty())
            return null;

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword()))
            return null;

        return user;
    }



    // CRUD ----------------------------------------------------
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }



    // ADMIN UPDATE: full user update --------------------------------------
    public User updateUser(Long id, User updated) {
        return userRepository.findById(id)
                .map(user -> {

                    if (updated.getName() != null)
                        user.setName(updated.getName());

                    if (updated.getUsername() != null)
                        user.setUsername(updated.getUsername());

                    if (updated.getEmail() != null)
                        user.setEmail(updated.getEmail());

                    if (updated.getPassword() != null)
                        user.setPassword(passwordEncoder.encode(updated.getPassword()));

                    if (updated.getPhone() != null)
                        user.setPhone(updated.getPhone());

                    if (updated.getAddress() != null)
                        user.setAddress(updated.getAddress());

                    if (updated.getBio() != null)
                        user.setBio(updated.getBio());

                    if (updated.getPicture() != null)
                        user.setPicture(updated.getPicture());

                    user.setPublicProfile(updated.isPublicProfile());

                    return userRepository.save(user);
                })
                .orElse(null);
    }



    // USER PROFILE UPDATE -----------------------------------------------
    public User updateProfile(Long id, User updated) {
        return userRepository.findById(id)
                .map(user -> {

                    if (updated.getPhone() != null)
                        user.setPhone(updated.getPhone());

                    if (updated.getAddress() != null)
                        user.setAddress(updated.getAddress());

                    if (updated.getBio() != null)
                        user.setBio(updated.getBio());

                    if (updated.getPicture() != null)
                        user.setPicture(updated.getPicture());

                    user.setPublicProfile(updated.isPublicProfile());

                    return userRepository.save(user);

                }).orElse(null);
    }

    //USER MY PROFILE UPDATE  -----------------------------------------------
    public User updateMyProfile(Long userId, UserUpdateRequest request) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) return null;

        User user = optionalUser.get();

        if (request.getName() != null)
            user.setName(request.getName());

        if (request.getUsername() != null)
            user.setUsername(request.getUsername());

        if (request.getPicture() != null)
            user.setPicture(request.getPicture());

        if (request.getPublicProfile() != null)
            user.setPublicProfile(request.getPublicProfile());

        if (request.getPhone() != null)
            user.setPhone(request.getPhone());

        if (request.getAddress() != null)
            user.setAddress(request.getAddress());

        if (request.getBio() != null)
            user.setBio(request.getBio());

        return userRepository.save(user);
    }



    // DELETE ----------------------------------------------------
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }



    // GOOGLE USER CREATION -----------------------------------------------
    public User createGoogleUser(String email, String name, String pictureUrl) {

        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setUsername(username);

        // Google accounts get a random password
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        user.setPicture(pictureUrl);

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        user.getRoles().add(userRole);

        return userRepository.save(user);
    }
}
