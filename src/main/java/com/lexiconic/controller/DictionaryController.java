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

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }


    @GetMapping
    public String dictionary() {
        return "dictionary";
    }

    @GetMapping("/{word}")
    public String lookup(@PathVariable String word, Model model) {
        WordDto wordDto = dictionaryService.getWord(word);
        model.addAttribute("word", wordDto);
        return "dictionary";
    }
}