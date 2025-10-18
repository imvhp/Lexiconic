package com.lexiconic.controller;

import com.lexiconic.domain.dto.FlashCardDto;
import com.lexiconic.domain.dto.WordDto;
import com.lexiconic.domain.entity.FlashCard;
import com.lexiconic.mapper.FlashCardMapper;
import com.lexiconic.mapper.WordMapper;
import com.lexiconic.service.FlashCardService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/decks/{deckId}")
public class DictionaryToFlashCardController {
    private final FlashCardService flashCardService;
    private final FlashCardMapper flashCardMapper;
    private final WordMapper wordMapper;

    public DictionaryToFlashCardController(FlashCardService flashCardService, FlashCardMapper flashCardMapper, WordMapper wordMapper) {
        this.flashCardService = flashCardService;
        this.flashCardMapper = flashCardMapper;
        this.wordMapper = wordMapper;
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
