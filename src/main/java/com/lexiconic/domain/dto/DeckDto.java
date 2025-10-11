package com.lexiconic.domain.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeckDto{
    private UUID id;
    private String name;
    private String description;
    private List<FlashCardDto> flashCards = new ArrayList<>();
}
