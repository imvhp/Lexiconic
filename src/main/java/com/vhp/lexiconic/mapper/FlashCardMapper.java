package com.vhp.lexiconic.mapper;

import com.vhp.lexiconic.domain.dto.FlashCardDto;
import com.vhp.lexiconic.domain.entity.FlashCard;
import org.springframework.stereotype.Component;

@Component
public class FlashCardMapper {
    public FlashCard toEntity(FlashCardDto dto){
        return new FlashCard(
                dto.id(),
                dto.word(),
                dto.pronunciation(),
                dto.partOfSpeech(),
                dto.audio(),
                dto.image(),
                dto.definition(),
                dto.example(),
                null,
                null,
                null
        );
    }

    public FlashCardDto toDto(FlashCard flashCard){
        return new FlashCardDto(
                flashCard.getId(),
                flashCard.getWord(),
                flashCard.getPronunciation(),
                flashCard.getPartOfSpeech(),
                flashCard.getAudio(),
                flashCard.getImage(),
                flashCard.getDefinition(),
                flashCard.getExample()
        );
    }

}
