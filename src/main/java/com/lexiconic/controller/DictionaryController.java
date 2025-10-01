package com.lexiconic.controller;

import com.lexiconic.domain.dto.DictionaryDto;
import com.lexiconic.domain.dto.UnsplashResponseDto;
import com.lexiconic.domain.dto.WordDto;
import com.lexiconic.mapper.WordMapper;
import com.lexiconic.service.DictionaryService;
import com.lexiconic.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final WordMapper wordMapper;
    private final ImageService imageService;

    public DictionaryController(DictionaryService dictionaryService, WordMapper wordMapper, ImageService imageService) {
        this.dictionaryService = dictionaryService;
        this.wordMapper = wordMapper;
        this.imageService = imageService;
    }


    @GetMapping
    public String dictionary() {
        return "dictionary";
    }

    @GetMapping("/{word}")
    public String lookup(@PathVariable String word, Model model) {
        DictionaryDto[] response = dictionaryService.fetchWordDefinition(word);
        UnsplashResponseDto unsplashResponseDto = imageService.getImageUrl(word);

        if (response == null || response.length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word not found");
        }

        WordDto wordDto =  wordMapper.toWordDto(response[0], unsplashResponseDto);
        model.addAttribute("word", wordDto);
        return "dictionary";
    }
}