package com.vhp.lexiconic.domain.dto;

import com.vhp.lexiconic.domain.entity.FlashCard;
import com.vhp.lexiconic.domain.entity.Users;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

public record DeckDto (
        UUID id, String name, String description, List<FlashCardDto> flashCards
){
}
