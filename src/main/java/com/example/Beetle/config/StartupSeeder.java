package com.example.Beetle.config;

import com.example.Beetle.model.PreferenceCard;
import com.example.Beetle.repository.PreferenceCardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class StartupSeeder {

    @Bean
    CommandLineRunner initPreferenceCards(PreferenceCardRepository repo) {
        return args -> {

            if (repo.count() == 0) {
                // Level 1 — Root categories
                PreferenceCard movies = repo.save(PreferenceCard.builder()
                        .label("Movies")
                        .key("movies")
                        .emoji("🎬")
                        .parentKey(null)
                        .level(1)
                        .active(true)
                        .build());

                PreferenceCard series = repo.save(PreferenceCard.builder()
                        .label("Series")
                        .key("series")
                        .emoji("📺")
                        .parentKey(null)
                        .level(1)
                        .active(true)
                        .build());

                PreferenceCard books = repo.save(PreferenceCard.builder()
                        .label("Books")
                        .key("books")
                        .emoji("📚")
                        .parentKey(null)
                        .level(1)
                        .active(true)
                        .build());

                PreferenceCard music = repo.save(PreferenceCard.builder()
                        .label("Music")
                        .key("music")
                        .emoji("🎵")
                        .parentKey(null)
                        .level(1)
                        .active(true)
                        .build());


// Level 2 — Movie genres
                PreferenceCard action = repo.save(PreferenceCard.builder()
                        .label("Action")
                        .key("action")
                        .emoji("💥")
                        .parentKey("movies")
                        .level(2)
                        .active(true)
                        .build());

                PreferenceCard drama = repo.save(PreferenceCard.builder()
                        .label("Drama")
                        .key("drama")
                        .emoji("🎭")
                        .parentKey("movies")
                        .level(2)
                        .active(true)
                        .build());

// Level 3 — Action formats
                PreferenceCard shortMovie = repo.save(PreferenceCard.builder()
                        .label("Short Movie (<90m)")
                        .key("action_short")
                        .emoji("⏱️")
                        .parentKey("action")
                        .level(3)
                        .active(true)
                        .build());

// Level 4 — Action actors
                repo.save(PreferenceCard.builder()
                        .label("Tom Cruise")
                        .key("tom_cruise_action_short")
                        .emoji("🕶️")
                        .parentKey("action_short")
                        .level(4)
                        .active(true)
                        .build());

            }
        };
    }
}
