package com.lexiconic.controller;

import com.lexiconic.domain.dto.ReviewRequestDto;
import com.lexiconic.domain.dto.ReviewResponseDto;
import com.lexiconic.domain.entity.Deck;
import com.lexiconic.domain.entity.FlashCard;
import com.lexiconic.repository.DeckRepository;
import com.lexiconic.repository.FlashCardRepository;
import com.lexiconic.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/decks/{deckId}/flashcards")
public class ReviewController {
    private final ReviewService reviewService;
    private final DeckRepository deckRepository;
    private final FlashCardRepository flashCardRepository;

    public ReviewController(ReviewService reviewService,DeckRepository deckRepository, FlashCardRepository flashCardRepository) {
        this.reviewService = reviewService;
        this.deckRepository = deckRepository;
        this.flashCardRepository = flashCardRepository;
    }

    @PostMapping("/reviews")
    public ReviewResponseDto reviewFlashCard(@RequestBody ReviewRequestDto request, @PathVariable UUID deckId) {
        Deck deck = deckRepository.findById(deckId).orElse(null);
        FlashCard flashCard = flashCardRepository.findById(request.flashCardId()).orElse(null);
        LocalDateTime nextReview = reviewService.calculateSuperMemo2Algorithm(deck, flashCard, request.quality());
        return new ReviewResponseDto(
                flashCard.getId(),
                nextReview == null ? LocalDateTime.now() : nextReview
        );
    }
}
