package com.example.Beetle.integration;

import com.example.Beetle.model.PreferenceCard;
import com.example.Beetle.repository.PreferenceCardRepository;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles ("test")
public class PreferenceCardIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PreferenceCardRepository repository;


    @Test
    void contextLoads() {

    }

    @Test
    void shouldReturnAllCards() throws Exception {
        mockMvc.perform(get("/api/preferences/cards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllCards_shouldReturn200_andJsonList() throws Exception {

        mockMvc.perform(get("/api/preferences/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    void getCardById_shouldReturn200_andCardJson() throws Exception {
        // Arrange — insert one card
        PreferenceCard card = repository.save(PreferenceCard.builder()
                        .label("Test Movie")
                        .key("test_movie")
                        .emoji("🎬")
                        .parentKey(null)
                        .level(1)
                        .active(true)
                        .build()
        );

        // Act & Assert
        mockMvc.perform(get("/api/preferences/cards/" + card.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Test Movie"))
                .andExpect(jsonPath("$.key").value("test_movie"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void getCardById_notFound_shouldReturn404() throws Exception {

        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(get("/api/preferences/cards/" + fakeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCard_shouldReturn200_andCreatedCard() throws Exception {

        PreferenceCard newCard = PreferenceCard.builder()
                .label("Sci-Fi")
                .key("sci_fi")
                .emoji("🚀")
                .parentKey("movies")
                .level(2)
                .active(true)
                .build();

        String body = objectMapper.writeValueAsString(newCard);

        mockMvc.perform(post("/api/preferences/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Sci-Fi"))
                .andExpect(jsonPath("$.key").value("sci_fi"))
                .andExpect(jsonPath("$.level").value(2));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createCard_asUser_shouldReturn403() throws Exception {

        PreferenceCard newCard = PreferenceCard.builder()
                .label("Detective")
                .key("detective")
                .level(2)
                .active(true)
                .build();

        String body = objectMapper.writeValueAsString(newCard);

        mockMvc.perform(post("/api/preferences/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }


}
