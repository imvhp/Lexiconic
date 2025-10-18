package com.lexiconic.controller;

import com.lexiconic.domain.dto.DeckDto;
import com.lexiconic.domain.dto.ReviewRequestDto;
import com.lexiconic.domain.dto.ReviewResponseDto;
import com.lexiconic.domain.entity.Deck;
import com.lexiconic.domain.entity.FlashCard;
import com.lexiconic.domain.entity.Review;
import com.lexiconic.domain.entity.Users;
import com.lexiconic.mapper.DeckMapper;
import com.lexiconic.repository.DeckRepository;
import com.lexiconic.repository.FlashCardRepository;
import com.lexiconic.repository.ReviewRepository;
import com.lexiconic.service.ReviewService;
import com.lexiconic.service.UserContextService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/decks/{deckId}/flashcards")
public class ReviewController {
    private final ReviewService reviewService;
    private final DeckRepository deckRepository;
    private final FlashCardRepository flashCardRepository;
    private final ReviewRepository reviewRepository;
    private final UserContextService userContextService;
    private final DeckMapper deckMapper;

    public ReviewController(ReviewService reviewService, DeckRepository deckRepository,
                            FlashCardRepository flashCardRepository,
                            ReviewRepository reviewRepository,
                            UserContextService userContextService,
                            DeckMapper deckMapper) {
        this.reviewService = reviewService;
        this.deckRepository = deckRepository;
        this.flashCardRepository = flashCardRepository;
        this.reviewRepository = reviewRepository;
        this.userContextService = userContextService;
        this.deckMapper = deckMapper;
    }

    @GetMapping
    public String flashcards(@PathVariable UUID deckId, Model model) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        Deck deck = deckRepository.findByIdAndOwner(deckId, currentUser);

        if (deck == null || !deck.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Deck not found or access denied");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);

        List<FlashCard> dueCards = deck.getFlashCards().stream()
                .filter(card -> {
                    Review lastReview = reviewRepository.findTopByFlashCardOrderByCreatedDesc(card);
                    boolean isDue = lastReview == null;
                    if (lastReview != null) {
                        LocalDateTime nextReviewDate = lastReview.getNextReview();
                        isDue = nextReviewDate != null && nextReviewDate.isBefore(endOfDay.plusSeconds(1));
                    }
                    return isDue;
                })
                .collect(Collectors.toList());

        model.addAttribute("deck", deckMapper.toDto(deck));
        model.addAttribute("flashCards", dueCards);
        model.addAttribute("hasDueCards", !dueCards.isEmpty());

        return "flashcards";
    }

    @GetMapping("/view")
    public String viewFlashcards(@PathVariable UUID deckId, Model model) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        Deck deck = deckRepository.findByIdAndOwner(deckId, currentUser);

        if (deck == null || !deck.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Deck not found or access denied");
        }

        DeckDto deckDto = deckMapper.toDto(deck);


        model.addAttribute("deck", deckDto);
        model.addAttribute("flashCards", deckDto.getFlashCards());
        model.addAttribute("viewMode", true);
        model.addAttribute("hasDueCards", false);

        return "flashcards";
    }

    @PostMapping("/reviews")
    @ResponseBody
    public ReviewResponseDto reviewFlashCard(@RequestBody ReviewRequestDto request, @PathVariable UUID deckId) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        Deck deck = deckRepository.findByIdAndOwner(deckId, currentUser);
        FlashCard flashCard = flashCardRepository.findById(request.flashCardId()).orElse(null);
        LocalDateTime nextReview = reviewService.calculateSuperMemo2Algorithm(deck, flashCard, request.quality());
        return new ReviewResponseDto(
                flashCard.getId(),
                nextReview == null ? LocalDateTime.now() : nextReview
        );
    }
}
