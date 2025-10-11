package com.lexiconic.service;

import com.lexiconic.domain.entity.Deck;
import com.lexiconic.domain.entity.FlashCard;
import com.lexiconic.repository.DeckRepository;
import com.lexiconic.repository.FlashCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FlashCardService {
    private final FlashCardRepository flashCardRepository;
    private final DeckRepository deckRepository;

    public FlashCardService(FlashCardRepository flashCardRepository, DeckRepository deckRepository) {
        this.flashCardRepository = flashCardRepository;
        this.deckRepository = deckRepository;
    }

    public FlashCard save(FlashCard flashCard, UUID deckId) {
        Deck deck = deckRepository.findById(deckId).orElse(null);
        if (deck != null) {
            if(flashCard.getId() != null) throw new IllegalArgumentException("id must be null");
            if(flashCard.getWord() == null || flashCard.getWord().isBlank()) throw new IllegalArgumentException("word must not be null or blank");

            LocalDateTime now = LocalDateTime.now();
            flashCard.setDeck(deck);
            flashCard.setCreated(now);
            flashCard.setUpdated(now);

            return flashCardRepository.save(flashCard);
        }
        return null;
    }

    @Transactional
    public FlashCard update(FlashCard flashCard, UUID deckId) {
        Deck deck = deckRepository.findById(deckId).orElse(null);
        if (deck != null) {
            FlashCard dbFlashCard = flashCardRepository.findById(flashCard.getId()).orElse(null);
            if (dbFlashCard != null) {
                dbFlashCard.setWord(flashCard.getWord());
                dbFlashCard.setPronunciation(flashCard.getPronunciation());
                dbFlashCard.setPartOfSpeech(flashCard.getPartOfSpeech());
                dbFlashCard.setAudio(flashCard.getAudio());
                dbFlashCard.setImage(flashCard.getImage());
                dbFlashCard.setDefinition(flashCard.getDefinition());
                dbFlashCard.setExample(flashCard.getExample());
                dbFlashCard.setUpdated(LocalDateTime.now());
                return flashCardRepository.save(dbFlashCard);
            }
        }
        return null;
    }



    @Transactional
    public void delete(UUID cardId, UUID deckId) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

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
