package com.lexiconic.controller;

import com.lexiconic.domain.dto.DeckDto;
import com.lexiconic.domain.entity.Deck;
import com.lexiconic.mapper.DeckMapper;
import com.lexiconic.repository.DeckRepository;
import com.lexiconic.service.DeckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/decks")
public class DeckController {
    private final DeckRepository deckRepository;
    private final DeckService deckService;
    private final DeckMapper deckMapper;

    public DeckController(DeckRepository deckRepository, DeckService deckService, DeckMapper deckMapper) {
        this.deckRepository = deckRepository;
        this.deckService = deckService;
        this.deckMapper = deckMapper;
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

    @PostMapping("create-deck")
    public DeckDto createDeck(@RequestBody DeckDto dto) {
        Deck deck = deckService.create(deckMapper.toEntity(dto));
        return deckMapper.toDto(deck);
    }

    @PutMapping("/edit-deck")
    public DeckDto updateDeck(@RequestBody DeckDto deckDto) {
        Deck deck = deckService.update(deckMapper.toEntity(deckDto));
        return deckMapper.toDto(deck);
    }

    @DeleteMapping("/delete-deck")
    public void deleteDeck(@RequestBody DeckDto deckDto) {
        deckService.delete(deckMapper.toEntity(deckDto));
    }

}
