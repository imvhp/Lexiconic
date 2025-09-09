package com.vhp.lexiconic.service;

import com.vhp.lexiconic.domain.entity.Deck;
import com.vhp.lexiconic.domain.entity.FlashCard;
import com.vhp.lexiconic.repository.DeckRepository;
import com.vhp.lexiconic.repository.FlashCardRepository;
import org.springframework.stereotype.Service;

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
            return flashCardRepository.save(
                    new FlashCard(
                            null,
                            flashCard.getWord(),
                            flashCard.getPronunciation(),
                            flashCard.getPartOfSpeech(),
                            flashCard.getAudio(),
                            flashCard.getImage(),
                            flashCard.getDefinition(),
                            flashCard.getExample(),
                            deck,
                            now,
                            now

                    )
            );
        }
        return null;
    }

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
                return flashCardRepository.save(dbFlashCard);
            }
        }
        return null;
    }

    public void delete(FlashCard flashCard, UUID deckId) {
        Deck deck = deckRepository.findById(deckId).orElse(null);
        if (deck != null) {
            FlashCard dbFlashCard = flashCardRepository.findById(flashCard.getId()).orElse(null);
            if (dbFlashCard != null) {
                flashCardRepository.delete(dbFlashCard);
            }
        }
    }



}
