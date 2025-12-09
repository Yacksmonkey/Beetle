package com.example.Beetle.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "card_key", nullable = false)
    private String cardKey;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;
}
