package com.vhp.lexiconic.controller;

import com.vhp.lexiconic.domain.dto.DeckDto;
import com.vhp.lexiconic.domain.entity.Deck;
import com.vhp.lexiconic.mapper.DeckMapper;
import com.vhp.lexiconic.repository.DeckRepository;
import com.vhp.lexiconic.service.DeckService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public List<DeckDto> allDecks() {
        return deckRepository.findAll()
                .stream()
                .map(deckMapper::toDto)
                .toList();
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
