package com.lexiconic.service;

import com.lexiconic.domain.entity.Deck;
import com.lexiconic.repository.DeckRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeckService {
    private final DeckRepository deckRepository;

    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public Deck create(Deck deck) {
        if(deck.getId() != null) throw new IllegalArgumentException("id must be null");
        if(deck.getName() == null || deck.getName().isBlank()) throw new IllegalArgumentException("title must not be null or blank");

        LocalDateTime now = LocalDateTime.now();
        return deckRepository.save(new Deck(
                null,
                deck.getName(),
                deck.getDescription(),
                null,
                null,
                now,
                now
        ));
    }

    public Deck update(Deck deck) {
        Deck dbdeck = deckRepository.findById(deck.getId()).orElse(null);
        if(dbdeck != null) {
            dbdeck.setName(deck.getName());
            dbdeck.setDescription(deck.getDescription());
            dbdeck.setUpdated(LocalDateTime.now());
            return deckRepository.save(dbdeck);
        }
        return null;
    }

    public void delete(Deck deck) {
        Deck dbdeck = deckRepository.findById(deck.getId()).orElse(null);
        if(dbdeck != null) {
            deckRepository.delete(dbdeck);
        }
    }
}
