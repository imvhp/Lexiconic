package com.lexiconic.controller;

import com.lexiconic.domain.dto.DeckDto;
import com.lexiconic.domain.dto.FlashCardDto;
import com.lexiconic.domain.entity.Users;
import com.lexiconic.mapper.DeckMapper;
import com.lexiconic.repository.DeckRepository;
import com.lexiconic.repository.UserRepository;
import com.lexiconic.service.DeckService;
import com.lexiconic.service.FlashCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserRepository userRepository;
    private final FlashCardService flashCardService;

    public DeckController(DeckRepository deckRepository, DeckService deckService, DeckMapper deckMapper, UserRepository userRepository, FlashCardService flashCardService) {
        this.deckRepository = deckRepository;
        this.deckService = deckService;
        this.deckMapper = deckMapper;
        this.userRepository = userRepository;
        this.flashCardService = flashCardService;
    }

    @GetMapping
    public String allDecks(Model model) {
        List<DeckDto> decks = deckRepository.findAll()
                .stream()
                .map(deckMapper::toDto)
                .toList();
        model.addAttribute("decks", decks);
        return "decks";
    }

    @GetMapping("/{deckId}")
    public String getDeck(@PathVariable UUID deckId, Model model) {
        DeckDto deckDto = deckRepository.findById(deckId)
                .map(deckMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Invalid deck id: " + deckId));
        model.addAttribute("deck", deckDto);
        return "deck";
    }

    @GetMapping("create-deck")
    public String getCreateDeckForm(Model model) {
        DeckDto deckDto = new DeckDto(null, "", "", new ArrayList<>());
        deckDto.getFlashCards().add(new FlashCardDto());
        model.addAttribute("deck", deckDto); // Add to the model

        return "deck";
    }

    @PostMapping
    public String createDeck(@ModelAttribute("deck") DeckDto dto,
                             @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        Users currentUser = userRepository.findByEmail(userDetails.getUsername());
        deckService.create(deckMapper.toEntity(dto), currentUser);
        return "redirect:/decks";
    }


    @PostMapping("/{deckId}")
    public String updateDeck(@PathVariable UUID deckId,
                                     @ModelAttribute("deck") DeckDto dto) {
        // 1. Ensure the ID from the path matches the ID in the DTO (safety check)
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
        deckService.delete(Objects.requireNonNull(deckRepository.findById(deckId).orElse(null)));
        return "redirect:/decks"; // Redirect to the list of all decks
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
