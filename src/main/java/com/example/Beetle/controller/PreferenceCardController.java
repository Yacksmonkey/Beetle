package com.example.Beetle.controller;

import com.example.Beetle.model.PreferenceCard;
import com.example.Beetle.repository.PreferenceCardRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/preferences/cards")
public class PreferenceCardController {

    private final PreferenceCardRepository repository;

    public PreferenceCardController(PreferenceCardRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<PreferenceCard>> findAll(
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String parentKey,
            @RequestParam(required = false) Boolean active
    ) {
        List<PreferenceCard> cards = repository.search(level, parentKey, active);
        return ResponseEntity.ok(cards);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PreferenceCard> findById(@PathVariable UUID id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PreferenceCard> create(@Valid @RequestBody PreferenceCard card) {
        card.setId(null); // prevent custom ids
        PreferenceCard saved = repository.save(card);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PreferenceCard> update(
            @PathVariable UUID id,
            @Valid @RequestBody PreferenceCard updatedCard
    ) {
        return repository.findById(id)
                .map(card -> {
                    card.setLabel(updatedCard.getLabel());
                    card.setKey(updatedCard.getKey());
                    card.setEmoji(updatedCard.getEmoji());
                    card.setImageUrl(updatedCard.getImageUrl());
                    card.setParentKey(updatedCard.getParentKey());
                    card.setLevel(updatedCard.getLevel());
                    card.setActive(updatedCard.getActive());
                    PreferenceCard saved = repository.save(card);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/toggle-active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PreferenceCard> toggleActive(@PathVariable UUID id) {
        return repository.findById(id)
                .map(card -> {
                    card.setActive(!card.getActive());
                    PreferenceCard saved = repository.save(card);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return repository.findById(id)
                .map(card -> {
                    repository.delete(card);
                    return ResponseEntity.ok("Card deleted");
                })
                .orElse(ResponseEntity.notFound().build());
    }


}
