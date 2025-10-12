package com.lexiconic.controller;

import com.lexiconic.domain.dto.FlashCardDto;
import com.lexiconic.domain.dto.WordDto;
import com.lexiconic.domain.entity.FlashCard;
import com.lexiconic.domain.entity.Users;
import com.lexiconic.mapper.FlashCardMapper;
import com.lexiconic.mapper.WordMapper;
import com.lexiconic.repository.FlashCardRepository;
import com.lexiconic.service.FlashCardService;
import com.lexiconic.service.UserContextService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/decks/{deckId}/flashcards")
public class FlashCardController {
    private final FlashCardService flashCardService;
    private final FlashCardMapper flashCardMapper;
    private final WordMapper wordMapper;

    public FlashCardController(FlashCardService flashCardService, FlashCardMapper flashCardMapper, WordMapper wordMapper) {
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
