package com.example.Beetle.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "preference_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceCard {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Label is required")
    @Size(max = 200, message = "Label must be at most 200 characters")
    @Column(nullable = false, length = 200)
    private String label; // Display label, e.g. "Action"

    @NotBlank(message = "Key is required")
    @Size(max = 100, message = "Key must be at most 100 characters")
    @Pattern(
            regexp = "^[a-z0-9_]+$",
            message = "Key must be lowercase, alphanumeric and may contain underscores"
    )
    @Column(name = "card_key", nullable = false, length = 100, unique = true)
    private String key; // Unique identifier used for logic or AI, e.g. "action"

    @Size(max = 10, message = "Emoji must be at most 10 characters")
    @Column(length = 10)
    private String emoji; // Optional emoji icon, e.g. "💥"

    @Size(max = 255, message = "Image URL must be at most 255 characters")
    @Column(name = "image_url")
    private String imageUrl; // Optional image icon or thumbnail URL

    @Size(max = 100, message = "Parent key must be at most 100 characters")
    @Column(name = "parent_key", length = 100)
    private String parentKey; // Key of parent card, null if top-level

    @NotNull(message = "Level is required")
    @Min(value = 1, message = "Level must be at least 1")
    @Max(value = 4, message = "Level must be at most 4")
    @Column(nullable = false)
    private Integer level; // Tree level (1=category, 2=genre, 3=format, 4=person)

    @NotNull(message = "Active flag is required")
    @Column(nullable = false)
    private Boolean active = true; // Allows disabling cards without deleting
}
