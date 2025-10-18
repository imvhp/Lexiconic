package com.lexiconic.service;

import com.lexiconic.domain.entity.Deck;
import com.lexiconic.domain.entity.FlashCard;
import com.lexiconic.domain.entity.Users;
import com.lexiconic.repository.DeckRepository;
import com.lexiconic.repository.FlashCardRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class DeckService {
    private final DeckRepository deckRepository;
    private final FlashCardService flashCardService;
    private final UserContextService userContextService;

    public DeckService(DeckRepository deckRepository, FlashCardService flashCardService, UserContextService userContextService) {
        this.deckRepository = deckRepository;
        this.flashCardService = flashCardService;
        this.userContextService = userContextService;
    }

    public Deck create(Deck deck) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        if(deck.getId() != null) throw new IllegalArgumentException("id must be null");
        if(deck.getName() == null || deck.getName().isBlank()) throw new IllegalArgumentException("title must not be null or blank");

        if(deck.getFlashCards() == null || deck.getFlashCards().isEmpty() || deck.getFlashCards().size() < 2) throw new IllegalArgumentException("deck must contain at least 2 flashcards, " + deck.getFlashCards().size() + " given");

        deck.setOwner(currentUser);

        LocalDateTime now = LocalDateTime.now();
        deck.setCreated(now);
        deck.setUpdated(now);

        // Link each flashcards to the deck
        for (FlashCard card : deck.getFlashCards()) {
            card.setDeck(deck);
            card.setCreated(now);
            card.setUpdated(now);
        }

        // Save both deck + flashcards at once via cascading
        return deckRepository.save(deck);
    }

    @Transactional
    public void update(Deck deck) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        Deck dbDeck = deckRepository.findByIdAndOwner(deck.getId(), currentUser);
        if(dbDeck == null) throw new AccessDeniedException("Deck not found or access denied");

        // Update deck fields
        dbDeck.setName(deck.getName());
        dbDeck.setDescription(deck.getDescription());
        dbDeck.setUpdated(LocalDateTime.now());

        List<FlashCard> dbFlashCards = dbDeck.getFlashCards();

        List<UUID> incomingIds = deck.getFlashCards().stream()
                .map(FlashCard::getId)
                .filter(Objects::nonNull)
                .toList();

        dbFlashCards.removeIf(fc -> !incomingIds.contains(fc.getId()));

        for (FlashCard card : deck.getFlashCards()) {
            if (card.getId() == null) {
                flashCardService.save(card, dbDeck.getId());
            }
            else {
                flashCardService.update(card, dbDeck.getId());
            }
        }


        deckRepository.save(dbDeck);
    }


    public void delete(Deck deck) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        Deck dbdeck = deckRepository.findByIdAndOwner(deck.getId(), currentUser);
        if(dbdeck != null) {
            deckRepository.delete(dbdeck);
        }
        else {
            throw new AccessDeniedException("Deck not found or access denied");
        }
    }
}
