package com.example.Beetle.service;

import com.example.Beetle.dto.CreateRecommendationRequest;
import com.example.Beetle.model.PreferenceCard;
import com.example.Beetle.model.Recommendation;
import com.example.Beetle.repository.PreferenceCardRepository;
import com.example.Beetle.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private PreferenceCardRepository preferenceCardRepository;

    public Recommendation create(CreateRecommendationRequest request) {

        if (request.getType() == null)
            throw new RuntimeException("Type is required");

        if (request.getTitle() == null || request.getTitle().isBlank())
            throw new RuntimeException("Title is required");

        if (request.getDescription() == null || request.getDescription().isBlank())
            throw new RuntimeException("Description is required");

        if (request.getCardKey() == null || request.getCardKey().isBlank())
            throw new RuntimeException("Card key is required");

        Recommendation rec = new Recommendation();
        rec.setType(request.getType());
        rec.setTitle(request.getTitle().trim());
        rec.setDescription(request.getDescription().trim());
        rec.setImageUrl(request.getImageUrl());
        rec.setExternalUrl(request.getExternalUrl());
        rec.setCardKey(request.getCardKey().trim());
        rec.setCreatedAt(OffsetDateTime.now());

        return recommendationRepository.save(rec);
    }

    public List<Recommendation> getRandomRecommendations() {
        List<Recommendation> all = recommendationRepository.findAll();
        Collections.shuffle(all);
        return all.stream().limit(3).toList();
    }

    public List<Recommendation> getByCardKey(String cardKey) {
        if (cardKey == null || cardKey.isBlank()) {
            throw new RuntimeException("Card key is required");
        }

        List<Recommendation> finalResults = new ArrayList<>();
        String currentKey = cardKey.trim();

        while (currentKey != null && !currentKey.isBlank() && finalResults.size() < 3) {
            List<Recommendation> currentResults = recommendationRepository.findByCardKey(currentKey);
            Collections.shuffle(currentResults);

            for (Recommendation rec : currentResults) {
                boolean alreadyExists = finalResults.stream()
                        .anyMatch(existing -> existing.getId().equals(rec.getId()));

                if (!alreadyExists) {
                    finalResults.add(rec);
                }

                if (finalResults.size() == 3) {
                    break;
                }
            }

            if (finalResults.size() == 3) {
                break;
            }

            Optional<PreferenceCard> cardOpt = preferenceCardRepository.findByKey(currentKey);

            if (cardOpt.isEmpty()) {
                break;
            }

            currentKey = cardOpt.get().getParentKey();
        }

        return finalResults;
    }
}