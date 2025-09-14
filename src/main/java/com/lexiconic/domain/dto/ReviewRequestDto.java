package com.lexiconic.domain.dto;

import java.util.UUID;

public record ReviewRequestDto(
        UUID flashCardId, int quality
) {
}
