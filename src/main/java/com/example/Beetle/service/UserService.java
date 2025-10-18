package com.example.Beetle.service;

import com.example.Beetle.model.User;
import com.example.Beetle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
                    user.setPassword(updatedUser.getPassword());
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

        userRepository.save(user);
        return "User registered successfully";
    }

    public String loginUser(String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getPassword().equals(password)) {
                return "Login successful";
            } else {
                return "Incorrect password";
            }
        } else {
            return "User not found";
        }
    }
}
