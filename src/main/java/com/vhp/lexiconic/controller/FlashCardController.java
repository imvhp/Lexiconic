package com.vhp.lexiconic.controller;

import com.vhp.lexiconic.domain.dto.FlashCardDto;
import com.vhp.lexiconic.domain.entity.FlashCard;
import com.vhp.lexiconic.mapper.FlashCardMapper;
import com.vhp.lexiconic.repository.FlashCardRepository;
import com.vhp.lexiconic.service.FlashCardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/decks/{deckId}/flashcards")
public class FlashCardController {
    private final FlashCardService flashCardService;
    private final FlashCardMapper flashCardMapper;
    private final FlashCardRepository flashCardRepository;

    public FlashCardController(FlashCardService flashCardService, FlashCardMapper flashCardMapper, FlashCardRepository flashCardRepository) {
        this.flashCardService = flashCardService;
        this.flashCardMapper = flashCardMapper;
        this.flashCardRepository = flashCardRepository;
    }

    @GetMapping
    public List<FlashCardDto> getFlashCards(@PathVariable UUID deckId) {
        return flashCardRepository
                .findAll()
                .stream()
                .map(flashCardMapper::toDto)
                .toList();
    }

    @PostMapping
    public FlashCardDto createFlashCard(@PathVariable UUID deckId, @RequestBody FlashCardDto flashCardDto) {
        FlashCard flashCard = flashCardService.save(flashCardMapper.toEntity(flashCardDto), deckId);
        return flashCardMapper.toDto(flashCard);
    }

    @PutMapping
    public FlashCardDto updateFlashCard(@PathVariable UUID deckId, @RequestBody FlashCardDto flashCardDto) {
        FlashCard flashCard = flashCardService.update(flashCardMapper.toEntity(flashCardDto), deckId);
        return flashCardMapper.toDto(flashCard);
    }

    @DeleteMapping
    public void deleteFlashCard(@PathVariable UUID deckId, @RequestBody FlashCardDto flashCardDto) {
        flashCardService.delete(flashCardMapper.toEntity(flashCardDto), deckId);
    }
}
