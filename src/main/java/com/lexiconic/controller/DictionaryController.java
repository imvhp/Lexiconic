package com.lexiconic.controller;

import com.lexiconic.domain.dto.DeckDto;
import com.lexiconic.domain.dto.DictionaryDto;
import com.lexiconic.domain.dto.UnsplashResponseDto;
import com.lexiconic.domain.dto.WordDto;
import com.lexiconic.domain.entity.Users;
import com.lexiconic.mapper.DeckMapper;
import com.lexiconic.mapper.WordMapper;
import com.lexiconic.repository.DeckRepository;
import com.lexiconic.service.DictionaryService;
import com.lexiconic.service.ImageService;
import com.lexiconic.service.UserContextService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final DeckRepository deckRepository;
    private final DeckMapper deckMapper;
    private final UserContextService userContextService;

    public DictionaryController(DictionaryService dictionaryService, DeckRepository deckRepository, DeckMapper deckMapper, UserContextService userContextService) {
        this.dictionaryService = dictionaryService;
        this.deckRepository = deckRepository;
        this.deckMapper = deckMapper;
        this.userContextService = userContextService;
    }


    @GetMapping
    public String dictionary() {
        return "dictionary";
    }

    @GetMapping("/{word}")
    public String lookup(@PathVariable String word, Model model, HttpServletRequest request) {
        WordDto wordDto = dictionaryService.getWord(word);
        model.addAttribute("word", wordDto);

        Users currentUser = userContextService.getCurrentUserOrThrow();
        if(currentUser != null) {
            List<DeckDto> decks = deckRepository.findAllByOwner(currentUser)
                    .stream()
                    .map(deckMapper::toDto)
                    .toList();
            model.addAttribute("decks", decks);
        }

        model.addAttribute("currentUri", request.getRequestURI());
        return "dictionary";
    }
}