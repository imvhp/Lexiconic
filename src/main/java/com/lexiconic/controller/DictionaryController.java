package com.lexiconic.controller;

import com.lexiconic.domain.dto.DictionaryDto;
import com.lexiconic.domain.dto.WordDto;
import com.lexiconic.mapper.FlashCardMapper;
import com.lexiconic.mapper.WordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private WordMapper wordMapper;

    @GetMapping("/{word}")
    public WordDto lookup(@PathVariable String word) {
        DictionaryDto[] response = restTemplate.getForObject(
                "https://www.dictionaryapi.com/api/v3/references/learners/json/" + word + "?key=" + apiKey,
                DictionaryDto[].class
        );

        if (response == null || response.length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word not found");
        }

        return wordMapper.toWordDto(response[0]);
    }






}
