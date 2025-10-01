package com.lexiconic.service;

import com.lexiconic.domain.dto.DictionaryDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DictionaryService {

    private static final String DICTIONARY_API_URL = "https://www.dictionaryapi.com/api/v3/references/learners/json/{word}";

    private final String apiKey;
    private final RestTemplate restTemplate;

    public DictionaryService(
            @Value("${merriam-webster.api.key}") String apiKey,
            RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
    }

    @Cacheable("dictionary")
    public DictionaryDto[] fetchWordDefinition(String word) {
        String url = UriComponentsBuilder.fromUriString(DICTIONARY_API_URL)
                .queryParam("key", apiKey)
                .buildAndExpand(word)
                .toUriString();

        return restTemplate.getForObject(url, DictionaryDto[].class);
    }
}
