package com.example.Beetle.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import jakarta.validation.constraints.*;

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
    private String label;

    @NotBlank(message = "Key is required")
    @Size(max = 100, message = "Key must be at most 100 characters")
    @Pattern(
            regexp = "^[a-z0-9_]+$",
            message = "Key must be lowercase, alphanumeric and may contain underscores"
    )
    @Column(name = "card_key", nullable = false, length = 100, unique = true)
    private String key;

    @Size(max = 30, message = "Emoji must be at most 10 characters")
    @Column(length = 30)
    private String emoji;

    @Size(max = 255, message = "Image URL must be at most 255 characters")
    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Size(max = 100, message = "Parent key must be at most 100 characters")
    @Column(name = "parent_key", length = 100)
    private String parentKey;

    @NotNull(message = "Level is required")
    @Min(value = 1, message = "Level must be at least 1")
    @Max(value = 5, message = "Level must be at most 5")
    @Column(nullable = false)
    private Integer level;

    @NotNull(message = "Active flag is required")
    @Column(nullable = false)
    private Boolean active = true;
}
