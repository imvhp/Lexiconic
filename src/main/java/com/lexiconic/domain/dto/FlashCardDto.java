package com.lexiconic.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashCardDto {
    private UUID id;
    private String word;
    private String pronunciation;
    private String partOfSpeech;
    private String audio;
    private String image;
    private String definition;
    private String example;
}

