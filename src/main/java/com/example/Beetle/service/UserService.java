package com.example.Beetle.service;

import com.example.Beetle.model.User;
import com.example.Beetle.security.JwtUtil;
import com.example.Beetle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.Beetle.model.Role;
import com.example.Beetle.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    return userRepository.save(user);
                })
                .orElse(null);
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public String registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Error: email is already registered";
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Error: username is already in use";
        }

        // 🔹 Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 🔹 Create roles if they do not exist
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        // 🔹 List of emails or usernames that will be administrators
        List<String> adminEmails = List.of("yacksmonkey@gmail.com");
        List<String> adminUsernames = List.of("yacksmonkey");

        // 🔹 Assign role according to conditions
        if (adminEmails.contains(user.getEmail()) || adminUsernames.contains(user.getUsername())) {
            user.getRoles().add(adminRole);
        } else {
            user.getRoles().add(userRole);
        }

        // 🔹 save the user with his role
        userRepository.save(user);

        return "User registered successfully with role(s): " +
                user.getRoles().stream().map(Role::getName).toList();
    }



    @Autowired
    private JwtUtil jwtUtil;

    public String loginUser(String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            return "User not found";
        }

        User user = existingUser.get();

        if (passwordEncoder.matches(password, user.getPassword())) {

            List<String> roles = user.getRoles().stream()
                    .map(role -> role.getName())
                    .toList();

            String token = jwtUtil.generateToken(user.getEmail(), roles);

            return "Bearer " + token;
        } else {
            return "Incorrect password";
        }

    }
}
