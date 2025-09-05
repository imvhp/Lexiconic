package com.vhp.lexiconic.mapper;

import com.vhp.lexiconic.domain.dto.DeckDto;
import com.vhp.lexiconic.domain.entity.Deck;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DeckMapper {

    private final FlashCardMapper flashCardsMapper;

    public DeckMapper(FlashCardMapper flashCardsMapper) {
        this.flashCardsMapper = flashCardsMapper;
    }

    public Deck toEntity(DeckDto dto){
        return new Deck(
                dto.id(),
                dto.name(),
                dto.description(),
                null,
                Optional.ofNullable(dto.flashCards())
                        .map(flashCards -> flashCards.stream()
                                .map(flashCardsMapper::toEntity)
                                .toList()
                        ).orElse(null),
                null,
                null
        );
    }

    public DeckDto toDto(Deck deck){
        return new DeckDto(
                deck.getId(),
                deck.getName(),
                deck.getDescription(),
                Optional.ofNullable(deck.getFlashCards())
                        .map(flashCards -> flashCards.stream()
                                .map(flashCardsMapper::toDto)
                                .toList()
                        ).orElse( null)
        );
    }
}
