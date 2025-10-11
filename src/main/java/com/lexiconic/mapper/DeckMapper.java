package com.lexiconic.mapper;

import com.lexiconic.domain.dto.DeckDto;
import com.lexiconic.domain.entity.Deck;
import com.lexiconic.domain.entity.FlashCard;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DeckMapper {

    private final FlashCardMapper flashCardsMapper;

    public DeckMapper(FlashCardMapper flashCardsMapper) {
        this.flashCardsMapper = flashCardsMapper;
    }

    public Deck toEntity(DeckDto dto) {
        Deck deck = new Deck(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                null,
                new ArrayList<>(), // start with empty list
                null,
                null
        );

        if (dto.getFlashCards() != null && !dto.getFlashCards().isEmpty()) {
            List<FlashCard> flashCards = dto.getFlashCards().stream()
                    .map(flashCardDto -> {
                        FlashCard card = flashCardsMapper.toEntity(flashCardDto);
                        card.setDeck(deck); // 🔥 critical line
                        return card;
                    })
                    .toList();
            deck.setFlashCards(flashCards);
        }

        return deck;
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
