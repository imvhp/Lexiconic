package com.lexiconic.controller;

import com.lexiconic.domain.dto.FlashCardDto;
import com.lexiconic.domain.dto.WordDto;
import com.lexiconic.domain.entity.FlashCard;
import com.lexiconic.mapper.FlashCardMapper;
import com.lexiconic.mapper.WordMapper;
import com.lexiconic.repository.FlashCardRepository;
import com.lexiconic.service.FlashCardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/decks/{deckId}/flashcards")
public class FlashCardController {
    private final FlashCardService flashCardService;
    private final FlashCardMapper flashCardMapper;
    private final FlashCardRepository flashCardRepository;
    private final WordMapper wordMapper;

    public FlashCardController(FlashCardService flashCardService, FlashCardMapper flashCardMapper, FlashCardRepository flashCardRepository, WordMapper wordMapper) {
        this.flashCardService = flashCardService;
        this.flashCardMapper = flashCardMapper;
        this.flashCardRepository = flashCardRepository;
        this.wordMapper = wordMapper;
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

    @PostMapping("/dictionary")
    public FlashCardDto wordToFlashCardDto(
            @PathVariable UUID deckId,
            @RequestBody WordDto wordDto
    ) {
        FlashCard flashCard = flashCardService.save(wordMapper.toEntity(wordDto), deckId);
        return flashCardMapper.toDto(flashCard);
    }
}
