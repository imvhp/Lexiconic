package com.vhp.lexiconic.domain.dto;

import com.vhp.lexiconic.domain.entity.Deck;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

public record FlashCardDto (
        UUID id, String word, String pronunciation, String partOfSpeech, String audio, String image, String definition, String example
) {
}
