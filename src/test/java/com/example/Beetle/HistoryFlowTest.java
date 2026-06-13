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
class HistoryFlowTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @Autowired private JwtUtil jwtUtil;

    private Long createdUserId;

    @AfterEach
    void cleanup() {
        // We only delete the user we created. FK cascade will clean history rows.
        if (createdUserId != null && userRepository.existsById(createdUserId)) {
            userRepository.deleteById(createdUserId);
        }
    }

    @Test
    void fullFlow_createRecommendation_save_list_delete() throws Exception {
        // 1) Create test user (only for token identity)
        User user = createTestUser();
        createdUserId = user.getId();

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                List.of("ROLE_USER")
        );

        // 2) Create recommendation
        String createRecJson = """
                {
                  "type": "MOVIE",
                  "title": "Inception",
                  "description": "Test description",
                  "imageUrl": null,
                  "externalUrl": null
                }
                """;

        String recResponse = mockMvc.perform(post("/api/recommendations")
                        .cookie(new jakarta.servlet.http.Cookie("auth_token", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRecJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long recId = extractLong(recResponse, "id");

        // 3) Save to history as SAVED
        String saveHistoryJson = """
                {
                  "recommendationId": %d,
                  "status": "SAVED"
                }
                """.formatted(recId);

        String historyResponse = mockMvc.perform(post("/api/history")
                        .cookie(new jakarta.servlet.http.Cookie("auth_token", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saveHistoryJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.historyId").exists())
                .andExpect(jsonPath("$.status").value("SAVED"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long historyId = extractLong(historyResponse, "historyId");

        // 4) List my history
        mockMvc.perform(get("/api/history/me?page=0&size=20")
                        .cookie(new jakarta.servlet.http.Cookie("auth_token", token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].historyId").value(historyId))
                .andExpect(jsonPath("$.items[0].recommendationId").value((int) recId))
                .andExpect(jsonPath("$.items[0].status").value("SAVED"));

        // 5) Delete history item
        mockMvc.perform(delete("/api/history/" + historyId)
                        .cookie(new jakarta.servlet.http.Cookie("auth_token", token)))
                .andExpect(status().isOk());

        // 6) Confirm it is gone
        mockMvc.perform(get("/api/history/me?page=0&size=20")
                        .cookie(new jakarta.servlet.http.Cookie("auth_token", token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(0));
    }

    private User createTestUser() {
        String uid = UUID.randomUUID().toString().substring(0, 8);

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        User u = new User();
        u.setName("Test " + uid);
        u.setUsername("test_" + uid);
        u.setEmail("test_" + uid + "@example.com");
        u.setPassword("encoded_dummy"); // not used in this test

        u.setRoles(new HashSet<>());
        u.getRoles().add(userRole);

        return userRepository.save(u);
    }

    // Tiny JSON helper without adding new libraries
    private static long extractLong(String json, String key) {
        // expects: "key": 123
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