package com.lexiconic.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryCache {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    String word;
    String pronunciation;
    String partOfSpeech;
    String audioUrl;
    String definition;
    String example;
    String imageUrl;
    LocalDateTime cachedAt;

}
