package com.lexiconic.controller;

import com.lexiconic.domain.dto.DeckDto;
import com.lexiconic.domain.dto.FlashCardDto;
import com.lexiconic.domain.entity.Deck;
import com.lexiconic.domain.entity.Users;
import com.lexiconic.mapper.DeckMapper;
import com.lexiconic.repository.DeckRepository;
import com.lexiconic.service.DeckService;
import com.lexiconic.service.FlashCardService;
import com.lexiconic.service.UserContextService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/decks")
public class DeckController {
    private final DeckRepository deckRepository;
    private final DeckService deckService;
    private final DeckMapper deckMapper;
    private final FlashCardService flashCardService;
    private final UserContextService userContextService;

    public DeckController(DeckRepository deckRepository, DeckService deckService, DeckMapper deckMapper, FlashCardService flashCardService, UserContextService userContextService) {
        this.deckRepository = deckRepository;
        this.deckService = deckService;
        this.deckMapper = deckMapper;
        this.flashCardService = flashCardService;
        this.userContextService = userContextService;
    }

    @GetMapping
    public String allDecks(Model model) {
        List<DeckDto> decks = deckRepository.findAllByOwner(userContextService.getCurrentUserOrThrow())
                .stream()
                .map(deckMapper::toDto)
                .toList();
        model.addAttribute("decks", decks);
        return "decks";
    }

    @GetMapping("/{deckId}")
    public String getDeck(@PathVariable UUID deckId, Model model) {
        Users currentUser = userContextService.getCurrentUserOrThrow();

        Deck deck = deckRepository.findByIdAndOwner(deckId, currentUser);
        if (deck == null) {
            throw new AccessDeniedException("Deck not found or access denied");
        }
        DeckDto deckDto = deckMapper.toDto(deck);
        model.addAttribute("deck", deckDto);
        return "deck";
    }

    @GetMapping("create-deck")
    public String getCreateDeckForm(Model model) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        if(currentUser == null){
            return "redirect:/auth/login";
        }
        DeckDto deckDto = new DeckDto(null, "", "", new ArrayList<>());
        deckDto.getFlashCards().add(new FlashCardDto());
        model.addAttribute("deck", deckDto); // Add to the model

        return "deck";
    }

    @PostMapping
    public String createDeck(@ModelAttribute("deck") DeckDto dto) {
        deckService.create(deckMapper.toEntity(dto));
        return "redirect:/decks";
    }


    @PostMapping("/{deckId}")
    public String updateDeck(@PathVariable UUID deckId,
                                     @ModelAttribute("deck") DeckDto dto) {

        if (!deckId.equals(dto.getId())) {
            throw new IllegalArgumentException("Deck ID mismatch!");
        }

        // 2. Use your existing deckService update logic
        deckService.update(deckMapper.toEntity(dto));

        // Redirect back to the updated deck's page
        return "redirect:/decks/" + deckId;
    }


    @DeleteMapping("/{deckId}")
    public String deleteDeck(@PathVariable UUID deckId) {
        Users currentUser = userContextService.getCurrentUserOrThrow();
        deckService.delete(Objects.requireNonNull(deckRepository.findByIdAndOwner(deckId, currentUser)));
        return "redirect:/decks";
    }

    @DeleteMapping("/{deckId}/flashcards/{cardId}")
    public ResponseEntity<?> deleteFlashcard(
            @PathVariable UUID deckId,
            @PathVariable UUID cardId
    ) {
        try {
            flashCardService.delete(cardId, deckId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
