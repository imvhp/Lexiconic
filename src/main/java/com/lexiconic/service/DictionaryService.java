package com.lexiconic.service;

import com.lexiconic.domain.dto.DictionaryDto;
import com.lexiconic.domain.dto.UnsplashResponseDto;
import com.lexiconic.domain.dto.WordDto;
import com.lexiconic.domain.entity.DictionaryCache;
import com.lexiconic.mapper.WordMapper;
import com.lexiconic.repository.DictionaryCacheRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@Service
public class DictionaryService {

    private static final String DICTIONARY_API_URL = "https://www.dictionaryapi.com/api/v3/references/learners/json/{word}";

    private final String apiKey;
    private final RestTemplate restTemplate;
    private final ImageService imageService;
    private final WordMapper wordMapper;
    private final DictionaryCacheRepository dictionaryCacheRepository;

    public DictionaryService(
            @Value("${merriam-webster.api.key}") String apiKey,
            RestTemplate restTemplate, ImageService imageService, WordMapper wordMapper, DictionaryCacheRepository dictionaryCacheRepository) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
        this.imageService = imageService;
        this.wordMapper = wordMapper;
        this.dictionaryCacheRepository = dictionaryCacheRepository;
    }

    private DictionaryDto[] fetchWordDefinition(String word) {
        String url = UriComponentsBuilder.fromUriString(DICTIONARY_API_URL)
                .queryParam("key", apiKey)
                .buildAndExpand(word)
                .toUriString();

        return restTemplate.getForObject(url, DictionaryDto[].class);
    }

    public WordDto getWord(String word){
        DictionaryCache dictionaryCache = dictionaryCacheRepository.findByWord(word);
        if(dictionaryCache != null){
            return wordMapper.dictionaryCacheToWordDto(dictionaryCache);
        }
        DictionaryDto[] response = fetchWordDefinition(word);
        UnsplashResponseDto unsplashResponseDto = imageService.getImageUrl(word);
         WordDto wordDto = wordMapper.dictionaryToWordDto(response[0], unsplashResponseDto);
        dictionaryCacheRepository.save(wordMapper.wordDtoToDictionaryCache(wordDto));
        return wordDto;
    }
}
