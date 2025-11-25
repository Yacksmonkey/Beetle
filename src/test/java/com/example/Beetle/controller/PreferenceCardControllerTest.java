package com.example.Beetle.controller;

import com.example.Beetle.model.PreferenceCard;
import com.example.Beetle.repository.PreferenceCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;


import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PreferenceCardControllerTest {

    @Mock
    private PreferenceCardRepository repository;

    @InjectMocks
    private PreferenceCardController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllCards() {

        PreferenceCard card1 = new PreferenceCard();
        card1.setId(UUID.randomUUID());
        card1.setLabel("Action");
        card1.setKey("action");

        PreferenceCard card2 = new PreferenceCard();
        card2.setId(UUID.randomUUID());
        card2.setLabel("Drama");
        card2.setKey("drama");

        when(repository.search(null, null, null))
                .thenReturn(List.of(card1, card2));

        ResponseEntity<List<PreferenceCard>> response =
                controller.findAll(null, null, null);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getLabel()).isEqualTo("Action");

        verify(repository, times(1))
                .search(null, null, null);
    }


    @Test
    void shouldReturnEmptyListWhenNoCardsExist() {

        when(repository.search(null, null, null))
                .thenReturn(List.of());

        ResponseEntity<List<PreferenceCard>> response =
                controller.findAll(null, null, null);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEmpty();

        verify(repository, times(1))
                .search(null, null, null);
    }


    @Test
    void shouldReturnCardWhenIdExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        PreferenceCard card = new PreferenceCard();
        card.setId(id);
        card.setLabel("Action");
        card.setKey("action");

        when(repository.findById(id)).thenReturn(java.util.Optional.of(card));

        // Act
        ResponseEntity<PreferenceCard> response = controller.findById(id);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getLabel()).isEqualTo("Action");

        // Verify
        verify(repository, times(1)).findById(id);
    }

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(java.util.Optional.empty());

        // Act
        ResponseEntity<PreferenceCard> response = controller.findById(id);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).isNull();

        // Verify
        verify(repository, times(1)).findById(id);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void shouldCreateNewCardAsAdmin() {

        // Arrange
        PreferenceCard newCard = new PreferenceCard();
        newCard.setLabel("Sci-Fi");
        newCard.setKey("sci_fi");
        newCard.setLevel(1);
        newCard.setActive(true);

        PreferenceCard savedCard = new PreferenceCard();
        savedCard.setId(UUID.randomUUID());
        savedCard.setLabel("Sci-Fi");
        savedCard.setKey("sci_fi");
        savedCard.setLevel(1);
        savedCard.setActive(true);

        when(repository.save(any(PreferenceCard.class))).thenReturn(savedCard);

        // Act
        ResponseEntity<PreferenceCard> response = controller.create(newCard);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getLabel()).isEqualTo("Sci-Fi");

        // Verify repository.save was called
        verify(repository, times(1)).save(any(PreferenceCard.class));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void shouldUpdateExistingCard() {
        // Arrange
        UUID id = UUID.randomUUID();
        PreferenceCard existing = new PreferenceCard();
        existing.setId(id);
        existing.setLabel("Action");
        existing.setKey("action");
        existing.setActive(true);

        PreferenceCard updated = new PreferenceCard();
        updated.setLabel("Action Updated");
        updated.setKey("action_updated");
        updated.setActive(true);

        when(repository.findById(id)).thenReturn(java.util.Optional.of(existing));
        when(repository.save(any(PreferenceCard.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        ResponseEntity<PreferenceCard> response = controller.update(id, updated);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getLabel()).isEqualTo("Action Updated");
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(any(PreferenceCard.class));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentCard() {
        // Arrange
        UUID id = UUID.randomUUID();
        PreferenceCard updated = new PreferenceCard();
        updated.setLabel("Does Not Exist");

        when(repository.findById(id)).thenReturn(java.util.Optional.empty());

        // Act
        ResponseEntity<PreferenceCard> response = controller.update(id, updated);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void shouldToggleActiveWhenCardExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        PreferenceCard card = new PreferenceCard();
        card.setId(id);
        card.setLabel("Drama");
        card.setKey("drama");
        card.setActive(true);

        when(repository.findById(id)).thenReturn(java.util.Optional.of(card));
        when(repository.save(any(PreferenceCard.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        ResponseEntity<PreferenceCard> response = controller.toggleActive(id);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getActive()).isFalse(); // toggled to false

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(any(PreferenceCard.class));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void shouldReturnNotFoundWhenTogglingNonexistentCard() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(java.util.Optional.empty());

        // Act
        ResponseEntity<PreferenceCard> response = controller.toggleActive(id);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any());
    }



}
