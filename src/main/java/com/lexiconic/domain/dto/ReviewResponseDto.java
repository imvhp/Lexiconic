package com.lexiconic.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponseDto (
        UUID flashCardId, LocalDateTime nextReview
){
}
