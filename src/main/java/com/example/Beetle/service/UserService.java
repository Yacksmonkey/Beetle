package com.example.Beetle.service;

import com.example.Beetle.model.Role;
import com.example.Beetle.model.User;
import com.example.Beetle.repository.RoleRepository;
import com.example.Beetle.repository.UserRepository;
import com.example.Beetle.security.JwtUtil;

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

    @Autowired
    private JwtUtil jwtUtil;



    // REGISTER USER
    // ---------------------------
    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Roles creation if missing
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        // Set admin accounts
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


    // LOGIN (Authentication)
    // ---------------------------

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


    public User authenticate(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }


    // CRUD operations
    // ---------------------------
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {

                    if (updatedUser.getName() != null)
                        user.setName(updatedUser.getName());

                    if (updatedUser.getUsername() != null)
                        user.setUsername(updatedUser.getUsername());

                    if (updatedUser.getEmail() != null)
                        user.setEmail(updatedUser.getEmail());

                    if (updatedUser.getPassword() != null)
                        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

                    return userRepository.save(user);
                })
                .orElse(null);
    }


    //DELETE USER
    //------------------------------------

    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    //CREATE GOOGLE
    //--------------------------

    public User createGoogleUser(String email, String name) {

        // Generate a base username from the email prefix
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;

        // Ensure the username is unique
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        // Create the user object
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setUsername(username);

        // Google users do not use a local password, but the field is NOT NULL
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        // Assign ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        user.getRoles().add(userRole);

        // Save the new user in the database
        return userRepository.save(user);
    }

}
