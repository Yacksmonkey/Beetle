package com.example.Beetle;

import com.example.Beetle.model.Role;
import com.example.Beetle.model.User;
import com.example.Beetle.repository.RoleRepository;
import com.example.Beetle.repository.UserRepository;
import com.example.Beetle.security.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HistoryOwnershipTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @Autowired private JwtUtil jwtUtil;

    private Long userAId;
    private Long userBId;

    @AfterEach
    void cleanup() {
        if (userAId != null && userRepository.existsById(userAId)) userRepository.deleteById(userAId);
        if (userBId != null && userRepository.existsById(userBId)) userRepository.deleteById(userBId);
    }

    @Test
    void userCannotDeleteOtherUsersHistoryItem() throws Exception {
        User userA = createTestUser();
        User userB = createTestUser();
        userAId = userA.getId();
        userBId = userB.getId();

        String tokenA = jwtUtil.generateToken(userA.getId(), userA.getEmail(), List.of("ROLE_USER"));
        String tokenB = jwtUtil.generateToken(userB.getId(), userB.getEmail(), List.of("ROLE_USER"));

        // Create recommendation (using A)
        String recJson = """
                {
                  "type": "MOVIE",
                  "title": "Other Test",
                  "description": "Desc",
                  "imageUrl": null,
                  "externalUrl": null
                }
                """;

        String recResponse = mockMvc.perform(post("/api/recommendations")
                        .cookie(new jakarta.servlet.http.Cookie("auth_token", tokenA))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        long recId = extractLong(recResponse, "id");

        // Save to history as A
        String saveJson = """
                { "recommendationId": %d, "status": "SAVED" }
                """.formatted(recId);

        String historyResponse = mockMvc.perform(post("/api/history")
                        .cookie(new jakarta.servlet.http.Cookie("auth_token", tokenA))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saveJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.historyId").exists())
                .andReturn().getResponse().getContentAsString();

        long historyId = extractLong(historyResponse, "historyId");

        // Try delete as B -> should be 403
        mockMvc.perform(delete("/api/history/" + historyId)
                        .cookie(new jakarta.servlet.http.Cookie("auth_token", tokenB)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Not allowed"));
    }

    private User createTestUser() {
        String uid = UUID.randomUUID().toString().substring(0, 8);

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        User u = new User();
        u.setName("Test " + uid);
        u.setUsername("test_" + uid);
        u.setEmail("test_" + uid + "@example.com");
        u.setPassword("encoded_dummy");

        u.setRoles(new HashSet<>());
        u.getRoles().add(userRole);

        return userRepository.save(u);
    }

    private static long extractLong(String json, String key) {
        String needle = "\"" + key + "\":";
        int i = json.indexOf(needle);
        if (i < 0) throw new RuntimeException("Key not found: " + key);
        int start = i + needle.length();
        while (start < json.length() && (json.charAt(start) == ' ')) start++;
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
        return Long.parseLong(json.substring(start, end));
    }
}