package com.example.Beetle.config;

import com.example.Beetle.model.PreferenceCard;
import com.example.Beetle.repository.PreferenceCardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class PreferenceCardSeeder {

    @Bean
    CommandLineRunner seedPreferenceCards(PreferenceCardRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                System.out.println("Preference cards already seeded. Skipping...");
                return;
            }

            List<PreferenceCard> cards = new ArrayList<>();

            // LEVEL 1
            cards.add(card("Movies", "movie", "🎬", null, 1));
            cards.add(card("Series", "series", "📺", null, 1));
            cards.add(card("Books", "book", "📚", null, 1));
            cards.add(card("Music", "music", "🎵", null, 1));

            // MOVIE - LEVEL 2
            cards.add(card("Action", "movie_action", "💥", "movie", 2));
            cards.add(card("Comedy", "movie_comedy", "😂", "movie", 2));
            cards.add(card("Drama", "movie_drama", "🎭", "movie", 2));
            cards.add(card("Sci-Fi", "movie_scifi", "🚀", "movie", 2));
            cards.add(card("Horror", "movie_horror", "👻", "movie", 2));
            cards.add(card("Romance", "movie_romance", "💕", "movie", 2));

            // MOVIE - LEVEL 3
            cards.add(card("Military", "movie_action_military", "🪖", "movie_action", 3));
            cards.add(card("Police", "movie_action_police", "🚔", "movie_action", 3));
            cards.add(card("Superhero", "movie_action_superhero", "🦸", "movie_action", 3));
            cards.add(card("Adventure", "movie_action_adventure", "🗺️", "movie_action", 3));

            cards.add(card("Romantic", "movie_comedy_romantic", "💘", "movie_comedy", 3));
            cards.add(card("Family", "movie_comedy_family", "👨‍👩‍👧‍👦", "movie_comedy", 3));
            cards.add(card("Dark", "movie_comedy_dark", "🌑", "movie_comedy", 3));
            cards.add(card("Parody", "movie_comedy_parody", "🎭", "movie_comedy", 3));

            cards.add(card("Historical", "movie_drama_historical", "🏛️", "movie_drama", 3));
            cards.add(card("Biography", "movie_drama_biography", "📖", "movie_drama", 3));
            cards.add(card("Emotional", "movie_drama_emotional", "😢", "movie_drama", 3));
            cards.add(card("Social", "movie_drama_social", "🌍", "movie_drama", 3));

            cards.add(card("Space", "movie_scifi_space", "🌌", "movie_scifi", 3));
            cards.add(card("Time Travel", "movie_scifi_time_travel", "⏳", "movie_scifi", 3));
            cards.add(card("AI", "movie_scifi_ai", "🤖", "movie_scifi", 3));
            cards.add(card("Dystopia", "movie_scifi_dystopia", "🏙️", "movie_scifi", 3));

            cards.add(card("Supernatural", "movie_horror_supernatural", "👻", "movie_horror", 3));
            cards.add(card("Slasher", "movie_horror_slasher", "🔪", "movie_horror", 3));
            cards.add(card("Psychological", "movie_horror_psychological", "🧠", "movie_horror", 3));
            cards.add(card("Monster", "movie_horror_monster", "👹", "movie_horror", 3));

            cards.add(card("Classic", "movie_romance_classic", "🎞️", "movie_romance", 3));
            cards.add(card("Teen", "movie_romance_teen", "🧑‍🤝‍🧑", "movie_romance", 3));
            cards.add(card("Drama", "movie_romance_drama", "🎭", "movie_romance", 3));
            cards.add(card("Comedy", "movie_romance_comedy", "😂", "movie_romance", 3));

            // MOVIE - LEVEL 4
            cards.add(card("Fast", "movie_action_military_fast", "⚡", "movie_action_military", 4));
            cards.add(card("Intense", "movie_action_military_intense", "🔥", "movie_action_military", 4));
            cards.add(card("Classic", "movie_action_police_classic", "🚓", "movie_action_police", 4));
            cards.add(card("Modern", "movie_action_police_modern", "🆕", "movie_action_police", 4));
            cards.add(card("Marvel Style", "movie_action_superhero_marvel", "🛡️", "movie_action_superhero", 4));
            cards.add(card("Dark Hero", "movie_action_superhero_dark", "🦇", "movie_action_superhero", 4));
            cards.add(card("Epic", "movie_action_adventure_epic", "🏔️", "movie_action_adventure", 4));
            cards.add(card("Fun", "movie_action_adventure_fun", "🎉", "movie_action_adventure", 4));

            cards.add(card("Light", "movie_comedy_romantic_light", "☀️", "movie_comedy_romantic", 4));
            cards.add(card("Modern", "movie_comedy_romantic_modern", "💖", "movie_comedy_romantic", 4));
            cards.add(card("Easy", "movie_comedy_family_easy", "😊", "movie_comedy_family", 4));
            cards.add(card("Popular", "movie_comedy_family_popular", "🔥", "movie_comedy_family", 4));
            cards.add(card("Absurd", "movie_comedy_dark_absurd", "🤪", "movie_comedy_dark", 4));
            cards.add(card("Cult", "movie_comedy_dark_cult", "🎬", "movie_comedy_dark", 4));
            cards.add(card("Spoof", "movie_comedy_parody_spoof", "🎭", "movie_comedy_parody", 4));
            cards.add(card("Meta", "movie_comedy_parody_meta", "🪞", "movie_comedy_parody", 4));

            cards.add(card("Awarded", "movie_drama_historical_awarded", "🏆", "movie_drama_historical", 4));
            cards.add(card("Classic", "movie_drama_historical_classic", "📜", "movie_drama_historical", 4));
            cards.add(card("True Story", "movie_drama_biography_true_story", "📘", "movie_drama_biography", 4));
            cards.add(card("Inspirational", "movie_drama_biography_inspirational", "✨", "movie_drama_biography", 4));
            cards.add(card("Slow", "movie_drama_emotional_slow", "🐢", "movie_drama_emotional", 4));
            cards.add(card("Heartbreaking", "movie_drama_emotional_heartbreaking", "💔", "movie_drama_emotional", 4));
            cards.add(card("Realistic", "movie_drama_social_realistic", "🎥", "movie_drama_social", 4));
            cards.add(card("Important", "movie_drama_social_important", "📢", "movie_drama_social", 4));

            cards.add(card("Visual", "movie_scifi_space_visual", "🎨", "movie_scifi_space", 4));
            cards.add(card("Space Epic", "movie_scifi_space_epic", "🪐", "movie_scifi_space", 4));
            cards.add(card("Complex", "movie_scifi_time_travel_complex", "🧩", "movie_scifi_time_travel", 4));
            cards.add(card("Mind-Bending", "movie_scifi_time_travel_mind_bending", "🌀", "movie_scifi_time_travel", 4));
            cards.add(card("Cold", "movie_scifi_ai_cold", "🧊", "movie_scifi_ai", 4));
            cards.add(card("Human vs Machine", "movie_scifi_ai_human_machine", "⚙️", "movie_scifi_ai", 4));
            cards.add(card("Oppressive", "movie_scifi_dystopia_oppressive", "🏢", "movie_scifi_dystopia", 4));
            cards.add(card("Rebellion", "movie_scifi_dystopia_rebellion", "✊", "movie_scifi_dystopia", 4));

            cards.add(card("Classic", "movie_horror_supernatural_classic", "🕯️", "movie_horror_supernatural", 4));
            cards.add(card("Modern", "movie_horror_supernatural_modern", "📺", "movie_horror_supernatural", 4));
            cards.add(card("Gore", "movie_horror_slasher_gore", "🩸", "movie_horror_slasher", 4));
            cards.add(card("Tension", "movie_horror_slasher_tension", "😨", "movie_horror_slasher", 4));
            cards.add(card("Dark", "movie_horror_psychological_dark", "🌑", "movie_horror_psychological", 4));
            cards.add(card("Twisted", "movie_horror_psychological_twisted", "🫨", "movie_horror_psychological", 4));
            cards.add(card("Classic Monster", "movie_horror_monster_classic", "🦖", "movie_horror_monster", 4));
            cards.add(card("Creature Feature", "movie_horror_monster_creature", "👾", "movie_horror_monster", 4));

            cards.add(card("Elegant", "movie_romance_classic_elegant", "🎀", "movie_romance_classic", 4));
            cards.add(card("Iconic", "movie_romance_classic_iconic", "💞", "movie_romance_classic", 4));
            cards.add(card("Cute", "movie_romance_teen_cute", "🌸", "movie_romance_teen", 4));
            cards.add(card("Emotional", "movie_romance_teen_emotional", "🥹", "movie_romance_teen", 4));
            cards.add(card("Sad", "movie_romance_drama_sad", "🥀", "movie_romance_drama", 4));
            cards.add(card("Intense", "movie_romance_drama_intense", "❤️‍🔥", "movie_romance_drama", 4));
            cards.add(card("Happy", "movie_romance_comedy_happy", "😊", "movie_romance_comedy", 4));
            cards.add(card("Modern Love", "movie_romance_comedy_modern_love", "❤️", "movie_romance_comedy", 4));

            // SERIES - LEVEL 2
            cards.add(card("Drama", "series_drama", "🎭", "series", 2));
            cards.add(card("Comedy", "series_comedy", "😂", "series", 2));
            cards.add(card("Crime", "series_crime", "🕵️", "series", 2));
            cards.add(card("Sci-Fi", "series_scifi", "🚀", "series", 2));
            cards.add(card("Fantasy", "series_fantasy", "🧙", "series", 2));
            cards.add(card("Documentary", "series_documentary", "🎥", "series", 2));

            // SERIES - LEVEL 3
            cards.add(card("Historical", "series_drama_historical", "🏛️", "series_drama", 3));
            cards.add(card("Psychological", "series_drama_psychological", "🧠", "series_drama", 3));
            cards.add(card("Family", "series_drama_family", "👨‍👩‍👧‍👦", "series_drama", 3));
            cards.add(card("Political", "series_drama_political", "🏛️", "series_drama", 3));

            cards.add(card("Sitcom", "series_comedy_sitcom", "🏠", "series_comedy", 3));
            cards.add(card("Workplace", "series_comedy_workplace", "💼", "series_comedy", 3));
            cards.add(card("Romantic", "series_comedy_romantic", "💕", "series_comedy", 3));
            cards.add(card("Dark", "series_comedy_dark", "🌑", "series_comedy", 3));

            cards.add(card("Detective", "series_crime_detective", "🔍", "series_crime", 3));
            cards.add(card("Mafia", "series_crime_mafia", "💼", "series_crime", 3));
            cards.add(card("Thriller", "series_crime_thriller", "⚠️", "series_crime", 3));
            cards.add(card("True Crime", "series_crime_true", "📂", "series_crime", 3));

            cards.add(card("Space", "series_scifi_space", "🌌", "series_scifi", 3));
            cards.add(card("Time Travel", "series_scifi_time_travel", "⏳", "series_scifi", 3));
            cards.add(card("AI", "series_scifi_ai", "🤖", "series_scifi", 3));
            cards.add(card("Dystopia", "series_scifi_dystopia", "🏙️", "series_scifi", 3));

            cards.add(card("Epic", "series_fantasy_epic", "⚔️", "series_fantasy", 3));
            cards.add(card("Magic", "series_fantasy_magic", "✨", "series_fantasy", 3));
            cards.add(card("Dark", "series_fantasy_dark", "🌑", "series_fantasy", 3));
            cards.add(card("Adventure", "series_fantasy_adventure", "🗺️", "series_fantasy", 3));

            cards.add(card("Nature", "series_doc_nature", "🌍", "series_documentary", 3));
            cards.add(card("History", "series_doc_history", "📜", "series_documentary", 3));
            cards.add(card("Science", "series_doc_science", "🔬", "series_documentary", 3));
            cards.add(card("Crime", "series_doc_crime", "🚨", "series_documentary", 3));

            // SERIES - LEVEL 4
            cards.add(card("Awarded", "series_drama_historical_awarded", "🏆", "series_drama_historical", 4));
            cards.add(card("Prestige", "series_drama_psychological_prestige", "👑", "series_drama_psychological", 4));
            cards.add(card("Comfort", "series_drama_family_comfort", "🫶", "series_drama_family", 4));
            cards.add(card("Politics", "series_drama_political_smart", "🎩", "series_drama_political", 4));

            cards.add(card("Classic Sitcom", "series_comedy_sitcom_classic", "📺", "series_comedy_sitcom", 4));
            cards.add(card("Office Hit", "series_comedy_workplace_hit", "🏢", "series_comedy_workplace", 4));
            cards.add(card("Modern Love", "series_comedy_romantic_modern_love", "❤️", "series_comedy_romantic", 4));
            cards.add(card("Cult Comedy", "series_comedy_dark_cult", "🎭", "series_comedy_dark", 4));

            cards.add(card("British Style", "series_crime_detective_british", "🇬🇧", "series_crime_detective", 4));
            cards.add(card("Antihero", "series_crime_mafia_antihero", "🕶️", "series_crime_mafia", 4));
            cards.add(card("Noir", "series_crime_thriller_noir", "🌃", "series_crime_thriller", 4));
            cards.add(card("Shocking", "series_crime_true_shocking", "⚡", "series_crime_true", 4));

            cards.add(card("Space Epic", "series_scifi_space_epic", "🪐", "series_scifi_space", 4));
            cards.add(card("Mind-Bending", "series_scifi_time_travel_mind", "🌀", "series_scifi_time_travel", 4));
            cards.add(card("Human vs Machine", "series_scifi_ai_human_machine", "⚙️", "series_scifi_ai", 4));
            cards.add(card("Rebellion", "series_scifi_dystopia_rebellion", "✊", "series_scifi_dystopia", 4));

            cards.add(card("Legendary", "series_fantasy_epic_legendary", "🐉", "series_fantasy_epic", 4));
            cards.add(card("Ancient Magic", "series_fantasy_magic_ancient", "📜", "series_fantasy_magic", 4));
            cards.add(card("Brutal", "series_fantasy_dark_brutal", "⚔️", "series_fantasy_dark", 4));
            cards.add(card("Hero Journey", "series_fantasy_adventure_hero", "🧭", "series_fantasy_adventure", 4));

            cards.add(card("BBC Style", "series_doc_nature_bbc", "🎙️", "series_doc_nature", 4));
            cards.add(card("World History", "series_doc_history_world", "🌐", "series_doc_history", 4));
            cards.add(card("Big Ideas", "series_doc_science_big_ideas", "💡", "series_doc_science", 4));
            cards.add(card("Case Files", "series_doc_crime_case_files", "📁", "series_doc_crime", 4));

            // BOOK - LEVEL 2
            cards.add(card("Fantasy", "book_fantasy", "🧙‍♂️", "book", 2));
            cards.add(card("Sci-Fi", "book_scifi", "🚀", "book", 2));
            cards.add(card("Romance", "book_romance", "💕", "book", 2));
            cards.add(card("Mystery", "book_mystery", "🔍", "book", 2));
            cards.add(card("Horror", "book_horror", "👻", "book", 2));

            // BOOK - LEVEL 3
            cards.add(card("Short (<300 pages)", "book_fantasy_short", "📖", "book_fantasy", 3));
            cards.add(card("Medium (300-500)", "book_fantasy_medium", "📘", "book_fantasy", 3));
            cards.add(card("Epic (500+)", "book_fantasy_epic", "📜", "book_fantasy", 3));

            cards.add(card("Space", "book_scifi_space", "🌌", "book_scifi", 3));
            cards.add(card("AI / Robots", "book_scifi_ai", "🤖", "book_scifi", 3));
            cards.add(card("Time Travel", "book_scifi_time_travel", "⏳", "book_scifi", 3));

            cards.add(card("Contemporary", "book_romance_contemporary", "❤️", "book_romance", 3));
            cards.add(card("Historical", "book_romance_historical", "🏛️", "book_romance", 3));
            cards.add(card("Fantasy", "book_romance_fantasy", "🔮", "book_romance", 3));

            cards.add(card("Detective", "book_mystery_detective", "🕵️", "book_mystery", 3));
            cards.add(card("Psychological", "book_mystery_psychological", "🧠", "book_mystery", 3));
            cards.add(card("Classic", "book_mystery_classic", "🕰️", "book_mystery", 3));

            cards.add(card("Supernatural", "book_horror_supernatural", "👻", "book_horror", 3));
            cards.add(card("Psychological", "book_horror_psychological", "🧠", "book_horror", 3));
            cards.add(card("Monster", "book_horror_monster", "👹", "book_horror", 3));

            cards.add(card("Dark Fantasy", "book_fantasy_dark", "🌑", "book_fantasy", 3));
            cards.add(card("Adventure", "book_fantasy_adventure", "🗺️", "book_fantasy", 3));
            cards.add(card("Dystopia", "book_scifi_dystopia", "🏙️", "book_scifi", 3));

            cards.add(card("Contemporary", "book_romance_contemporary_2", "💌", "book_romance", 3));
            cards.add(card("Crime", "book_mystery_crime", "🚨", "book_mystery", 3));
            cards.add(card("Monster", "book_horror_monster_2", "👹", "book_horror", 3));

            // BOOK - LEVEL 4
            cards.add(card("Tolkien Style", "book_fantasy_epic_tolkien", "🧙", "book_fantasy_epic", 4));
            cards.add(card("Dark Fantasy", "book_fantasy_epic_dark", "🌑", "book_fantasy_epic", 4));
            cards.add(card("Magic World", "book_fantasy_medium_magic", "✨", "book_fantasy_medium", 4));
            cards.add(card("Fast Adventure", "book_fantasy_short_fast", "⚡", "book_fantasy_short", 4));

            cards.add(card("Deep Space", "book_scifi_space_deep", "🪐", "book_scifi_space", 4));
            cards.add(card("Cold AI", "book_scifi_ai_cold", "🧊", "book_scifi_ai", 4));
            cards.add(card("Mind-Bending", "book_scifi_time_travel_mind", "🌀", "book_scifi_time_travel", 4));

            cards.add(card("Sweet", "book_romance_contemporary_sweet", "💗", "book_romance_contemporary", 4));
            cards.add(card("Elegant", "book_romance_historical_elegant", "🎀", "book_romance_historical", 4));
            cards.add(card("Spicy", "book_romance_fantasy_spicy", "🌶️", "book_romance_fantasy", 4));

            cards.add(card("Classic Detective", "book_mystery_detective_classic", "📖", "book_mystery_detective", 4));
            cards.add(card("Twisted", "book_mystery_psychological_twisted", "🫨", "book_mystery_psychological", 4));
            cards.add(card("Golden Age", "book_mystery_classic_golden_age", "🏅", "book_mystery_classic", 4));

            cards.add(card("Classic Ghost", "book_horror_supernatural_classic", "🕯️", "book_horror_supernatural", 4));
            cards.add(card("Dark Mind", "book_horror_psychological_dark", "🌑", "book_horror_psychological", 4));
            cards.add(card("Creature Feature", "book_horror_monster_creature", "👾", "book_horror_monster", 4));

            cards.add(card("Brutal", "book_fantasy_dark_brutal", "⚔️", "book_fantasy_dark", 4));
            cards.add(card("Hero Journey", "book_fantasy_adventure_hero", "🧭", "book_fantasy_adventure", 4));
            cards.add(card("Rebellion", "book_scifi_dystopia_rebellion", "✊", "book_scifi_dystopia", 4));

            cards.add(card("Heartbreaking", "book_romance_contemporary_heartbreaking", "💔", "book_romance_contemporary_2", 4));
            cards.add(card("Noir", "book_mystery_crime_noir", "🌃", "book_mystery_crime", 4));
            cards.add(card("Creature Feature", "book_horror_monster_creature_2", "👾", "book_horror_monster_2", 4));

            // MUSIC - LEVEL 2
            cards.add(card("Pop", "music_pop", "🎤", "music", 2));
            cards.add(card("Rock", "music_rock", "🎸", "music", 2));
            cards.add(card("Hip-Hop", "music_hiphop", "🎧", "music", 2));
            cards.add(card("Electronic", "music_electronic", "🎹", "music", 2));
            cards.add(card("Chill", "music_chill", "🌙", "music", 2));

            // MUSIC - LEVEL 3
            cards.add(card("Happy Mood", "music_pop_happy", "😄", "music_pop", 3));
            cards.add(card("Love Songs", "music_pop_love", "❤️", "music_pop", 3));
            cards.add(card("Dance Hits", "music_pop_dance", "🕺", "music_pop", 3));

            cards.add(card("Classic", "music_rock_classic", "🔥", "music_rock", 3));
            cards.add(card("Alternative", "music_rock_alt", "🌿", "music_rock", 3));
            cards.add(card("Heavy", "music_rock_heavy", "⚡", "music_rock", 3));

            cards.add(card("Trap", "music_hiphop_trap", "💰", "music_hiphop", 3));
            cards.add(card("Old School", "music_hiphop_oldschool", "📼", "music_hiphop", 3));
            cards.add(card("Freestyle", "music_hiphop_freestyle", "🎙️", "music_hiphop", 3));

            cards.add(card("House", "music_electronic_house", "🏠", "music_electronic", 3));
            cards.add(card("Techno", "music_electronic_techno", "🔊", "music_electronic", 3));
            cards.add(card("Ambient", "music_electronic_ambient", "🌫️", "music_electronic", 3));

            cards.add(card("Lo-fi", "music_chill_lofi", "☕", "music_chill", 3));
            cards.add(card("Sleep", "music_chill_sleep", "😴", "music_chill", 3));
            cards.add(card("Focus", "music_chill_focus", "🧠", "music_chill", 3));

            cards.add(card("Sad Mood", "music_pop_sad", "🥀", "music_pop", 3));
            cards.add(card("Soft Rock", "music_rock_soft", "🌤️", "music_rock", 3));

            cards.add(card("Conscious", "music_hiphop_conscious", "🧠", "music_hiphop", 3));
            cards.add(card("Synthwave", "music_electronic_synthwave", "🌆", "music_electronic", 3));
            cards.add(card("Deep Focus", "music_chill_deep_focus", "🎯", "music_chill", 3));

            // MUSIC - LEVEL 4
            cards.add(card("Mainstream", "music_pop_happy_mainstream", "🌟", "music_pop_happy", 4));
            cards.add(card("Breakup Anthem", "music_pop_love_breakup", "💔", "music_pop_love", 4));
            cards.add(card("Party", "music_pop_dance_party", "🎉", "music_pop_dance", 4));

            cards.add(card("70s/80s", "music_rock_classic_70s80s", "📻", "music_rock_classic", 4));
            cards.add(card("Indie Rock", "music_rock_alt_indie", "🖤", "music_rock_alt", 4));
            cards.add(card("Gym", "music_rock_heavy_gym", "🏋️", "music_rock_heavy", 4));

            cards.add(card("Late Night", "music_hiphop_trap_late_night", "🌙", "music_hiphop_trap", 4));
            cards.add(card("Golden Era", "music_hiphop_oldschool_golden", "🏅", "music_hiphop_oldschool", 4));
            cards.add(card("Heartfelt", "music_hiphop_freestyle_heartfelt", "🫀", "music_hiphop_freestyle", 4));

            cards.add(card("Festival", "music_electronic_house_festival", "🎪", "music_electronic_house", 4));
            cards.add(card("Underground", "music_electronic_techno_underground", "🕳️", "music_electronic_techno", 4));
            cards.add(card("Meditation", "music_electronic_ambient_meditation", "🧘", "music_electronic_ambient", 4));

            cards.add(card("Study", "music_chill_lofi_study", "📚", "music_chill_lofi", 4));
            cards.add(card("Sleep Deep", "music_chill_sleep_deep", "🛌", "music_chill_sleep", 4));
            cards.add(card("Breakup Songs", "music_pop_sad_breakup", "💔", "music_pop_sad", 4));

            cards.add(card("Road Trip", "music_rock_soft_roadtrip", "🚗", "music_rock_soft", 4));
            cards.add(card("Thoughtful Bars", "music_hiphop_conscious_thoughtful", "💭", "music_hiphop_conscious", 4));
            cards.add(card("80s Neon", "music_electronic_synthwave_neon", "📼", "music_electronic_synthwave", 4));

            cards.add(card("Work Session", "music_chill_deep_focus_work", "💼", "music_chill_deep_focus", 4));
            cards.add(card("Work Focus", "music_chill_focus_work", "💼", "music_chill_focus", 4));

            repository.saveAll(cards);
            System.out.println("Preference cards seeded successfully.");
        };
    }

    private PreferenceCard card(String label, String key, String emoji, String parentKey, int level) {
        return PreferenceCard.builder()
                .label(label)
                .key(key)
                .emoji(emoji)
                .imageUrl(null)
                .parentKey(parentKey)
                .level(level)
                .active(true)
                .build();
    }
}