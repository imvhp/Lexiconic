package com.lexiconic.mapper;

import com.lexiconic.domain.dto.FlashCardDto;
import com.lexiconic.domain.entity.FlashCard;
import org.springframework.stereotype.Component;

@Component
public class FlashCardMapper {
    public FlashCard toEntity(FlashCardDto dto){
        return new FlashCard(
                dto.getId(),
                dto.getWord(),
                dto.getPronunciation(),
                dto.getPartOfSpeech(),
                dto.getAudio(),
                dto.getImage(),
                dto.getDefinition(),
                dto.getExample(),
                null,
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
