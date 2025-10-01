package com.lexiconic.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lexiconic.domain.dto.DictionaryDto;
import com.lexiconic.domain.dto.UnsplashResponseDto;
import com.lexiconic.domain.dto.WordDto;
import com.lexiconic.domain.entity.FlashCard;
import org.springframework.stereotype.Component;


@Component
public class WordMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public WordDto toWordDto(DictionaryDto dto, UnsplashResponseDto unsplash) {
        String word = null;

        // 1) try app-shortdef.hw
        if (dto.meta() != null && dto.meta().appShortdef() != null) {
            String hw = dto.meta().appShortdef().hw();
            if (hw != null && !hw.isBlank())  {
                word = cleanText(hw);
            }
        }

        String pronunciation = dto.hwi().prs() != null && !dto.hwi().prs().isEmpty()
                ? dto.hwi().prs().getFirst().ipa()
                : null;
        String audio = dto.hwi().prs() != null && !dto.hwi().prs().isEmpty()
                ? buildAudioUrl(dto.hwi().prs().getFirst().sound().audio())
                : null;
        String partOfSpeech = dto.fl();

        // definition + example
        String definition = null;
        String example = null;

        try {
            JsonNode sseq = objectMapper.valueToTree(dto.def().getFirst().sseq());
            JsonNode senseContent = sseq.get(0).get(0).get(1); // "sense" content
            JsonNode dtArray = senseContent.get("dt");

            for (JsonNode dt : dtArray) {
                String type = dt.get(0).asText();

                if ("text".equals(type) && definition == null) {
                    definition = cleanText(dt.get(1).asText());
                }

                if ("vis".equals(type) && example == null) {
                    JsonNode visArray = dt.get(1);
                    if (visArray.isArray() && !visArray.isEmpty()) {
                        example = cleanText(visArray.get(0).get("t").asText());
                    }
                }
            }
        } catch (Exception e) {
            // fallback if structure is weird
            definition = "No definition available";
        }

        String imageUrl = (unsplash != null
                && unsplash.results() != null
                && !unsplash.results().isEmpty())
                ? unsplash.results().getFirst().urls().small()
                : "default-placeholder-url";

        return new WordDto(word, pronunciation, partOfSpeech, audio, definition, example, imageUrl);
    }
    public String buildAudioUrl(String audio) {
        // Merriam-Webster audio URL rules
        String subdir;
        if (audio.startsWith("bix")) subdir = "bix";
        else if (audio.startsWith("gg")) subdir = "gg";
        else if (Character.isDigit(audio.charAt(0)) || audio.charAt(0) == '_') subdir = "number";
        else subdir = String.valueOf(audio.charAt(0));

        return "https://media.merriam-webster.com/audio/prons/en/us/mp3/"
                + subdir + "/" + audio + ".mp3";
    }

    private static String cleanText(String raw) {
        if (raw == null) return null;
        // remove '*' syllable markers, MW tokens like {it}, also trim whitespace
        String cleaned = raw.replace("*", "")                     // remove asterisks
                .replaceAll("\\{[^}]+}", "")          // remove MW tokens like {bc}, {it}
                .replaceAll(":\\d+$", "")             // safety: strip homograph tail if still present
                .trim();
        return cleaned;
    }


    public FlashCard toEntity(WordDto word) {
        return new FlashCard(
                null,
                word.word(),
                word.pronunciation(),
                word.partOfSpeech(),
                word.audioUrl(),
                word.imageUrl(),
                word.definition(),
                word.example(),
                null,
                null,
                null,
                null
        );
    }
}
