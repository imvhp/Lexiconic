package com.lexiconic.domain.dto;

import java.util.List;
import java.util.UUID;

public record DeckDto (
        UUID id, String name, String description, List<FlashCardDto> flashCards
){
}
