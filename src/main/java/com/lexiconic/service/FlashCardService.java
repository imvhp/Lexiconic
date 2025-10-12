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
import java.util.UUID;

@Service
public class FlashCardService {
    private final FlashCardRepository flashCardRepository;
    private final DeckRepository deckRepository;
    private final UserContextService userContextService;

    public FlashCardService(FlashCardRepository flashCardRepository, DeckRepository deckRepository, UserContextService userContextService) {
        this.flashCardRepository = flashCardRepository;
        this.deckRepository = deckRepository;
        this.userContextService = userContextService;
    }

    public FlashCard save(FlashCard flashCard, UUID deckId) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        Deck deck = deckRepository.findByIdAndOwner(deckId, currentUser);
        if (deck != null) {
            if(flashCard.getId() != null) throw new IllegalArgumentException("id must be null");
            if(flashCard.getWord() == null || flashCard.getWord().isBlank()) throw new IllegalArgumentException("word must not be null or blank");

            LocalDateTime now = LocalDateTime.now();
            flashCard.setDeck(deck);
            flashCard.setCreated(now);
            flashCard.setUpdated(now);

            return flashCardRepository.save(flashCard);
        }
        throw new AccessDeniedException ("Deck not found or access denied");
    }

    @Transactional
    public void update(FlashCard flashCard, UUID deckId) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        Deck deck = deckRepository.findByIdAndOwner(deckId, currentUser);
        if (deck != null) {
            FlashCard dbFlashCard = flashCardRepository.findById(flashCard.getId()).orElse(null);
            if (dbFlashCard == null) {
                throw new IllegalArgumentException("FlashCard not found");
            }
            if (!dbFlashCard.getDeck().getId().equals(deckId)) {
                throw new AccessDeniedException("FlashCard does not belong to this deck");
            }
            else {
                dbFlashCard.setWord(flashCard.getWord());
                dbFlashCard.setPronunciation(flashCard.getPronunciation());
                dbFlashCard.setPartOfSpeech(flashCard.getPartOfSpeech());
                dbFlashCard.setAudio(flashCard.getAudio());
                dbFlashCard.setImage(flashCard.getImage());
                dbFlashCard.setDefinition(flashCard.getDefinition());
                dbFlashCard.setExample(flashCard.getExample());
                dbFlashCard.setUpdated(LocalDateTime.now());
                flashCardRepository.save(dbFlashCard);
            }
        }
        else {
            throw new AccessDeniedException ("Deck not found or access denied");
        }
    }



    @Transactional
    public void delete(UUID cardId, UUID deckId) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        Deck deck = deckRepository.findByIdAndOwner(deckId, currentUser);
        if (deck == null) {
            throw new AccessDeniedException("Deck not found or access denied");
        }

        if (deck.getFlashCards().size() <= 2) {
            throw new IllegalArgumentException("Deck must contain at least 2 cards");
        }

        FlashCard flashCard = deck.getFlashCards()
                .stream()
                .filter(fc -> fc.getId().equals(cardId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        deck.getFlashCards().remove(flashCard);
    }



}
