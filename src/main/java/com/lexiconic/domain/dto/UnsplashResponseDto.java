package com.lexiconic.domain.dto;

import java.util.List;

public record UnsplashResponseDto(
        List<Result> results
) {
    public record Result(
            String id,
            String alt_description,
            Urls urls
    ) {}

    public record Urls(
            String small,
            String regular
    ) {}
}

