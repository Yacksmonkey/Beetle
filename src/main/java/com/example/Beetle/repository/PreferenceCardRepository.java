package com.example.Beetle.repository;

import com.example.Beetle.model.PreferenceCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PreferenceCardRepository extends JpaRepository<PreferenceCard, UUID> {

    @Query("""
    SELECT c FROM PreferenceCard c
    WHERE (:level IS NULL OR c.level = :level)
      AND (:parentKey IS NULL OR c.parentKey = :parentKey)
      AND (:active IS NULL OR c.active = :active)
    """)
    List<PreferenceCard> search(
            @Param("level") Integer level,
            @Param("parentKey") String parentKey,
            @Param("active") Boolean active
    );

    Optional<PreferenceCard> findByKey(String key);
}
