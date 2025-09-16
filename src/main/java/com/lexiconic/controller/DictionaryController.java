package com.lexiconic.controller;

import com.lexiconic.domain.dto.DictionaryDto;
import com.lexiconic.domain.dto.WordDto;
import com.lexiconic.mapper.WordMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    private static final String DICTIONARY_API_URL = "https://www.dictionaryapi.com/api/v3/references/learners/json/{word}";
    
    private final String apiKey;
    private final RestTemplate restTemplate;
    private final WordMapper wordMapper;

    public DictionaryController(
            @Value("${api.key}") String apiKey,
            RestTemplate restTemplate,
            WordMapper wordMapper) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
        this.wordMapper = wordMapper;
    }

    @GetMapping("/{word}")
    public WordDto lookup(@PathVariable String word) {
        DictionaryDto[] response = fetchWordDefinition(word);

        if (response == null || response.length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word not found");
        }

        return wordMapper.toWordDto(response[0]);
    }
    
    private DictionaryDto[] fetchWordDefinition(String word) {
        String url = UriComponentsBuilder.fromUriString(DICTIONARY_API_URL)
                .queryParam("key", apiKey)
                .buildAndExpand(word)
                .toUriString();
                
        return restTemplate.getForObject(url, DictionaryDto[].class);
    }
}