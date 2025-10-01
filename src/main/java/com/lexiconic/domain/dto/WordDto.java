package com.lexiconic.domain.dto;

public record WordDto(
        String word,
        String pronunciation,
        String partOfSpeech,
        String audioUrl,
        String definition,
        String example,
        String imageUrl
) {}

