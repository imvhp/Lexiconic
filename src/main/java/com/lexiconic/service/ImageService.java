package com.lexiconic.service;

import com.lexiconic.domain.dto.UnsplashResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ImageService {

    private static final String IMAGE_API_URL = "https://api.unsplash.com/search/photos?query={keyword}&client_id={uns}";

    private final String unsplashApiKey;
    private final RestTemplate restTemplate;

    public ImageService(
            @Value("${unsplash.api.key}") String unsplashApiKey,
            RestTemplate restTemplate) {
        this.unsplashApiKey = unsplashApiKey;
        this.restTemplate = restTemplate;
    }

    @Cacheable("image")
    public UnsplashResponseDto getImageUrl(String word) {
        String url = IMAGE_API_URL
                .replace("{keyword}", word)
                .replace("{uns}", unsplashApiKey);

        return restTemplate.getForObject(url, UnsplashResponseDto.class);
    }
}
