package com.vhp.lexiconic.domain.dto;

import java.util.UUID;

public record FlashCardDto (
        UUID id, String word, String pronunciation, String partOfSpeech, String audio, String image, String definition, String example
) {
}
