package com.lexiconic.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DictionaryDto(
        Meta meta,
        Hwi hwi,
        String fl,
        List<Def> def,
        List<String> shortdef
) {
    public record Meta(
            String id,
            String uuid,
            String src,
            String section,
            List<String> stems,
            @JsonProperty("app-shortdef") AppShortdef appShortdef,
            boolean offensive,
            String highlight
    ) {
        public record AppShortdef(
            String hw,
            String fl,
            List<String> def
        ) {}}


    public record Hwi(
            String hw,
            List<Pr> prs
    ) {
        public record Pr(
                String ipa,
                Sound sound
        ) {
            public record Sound(
                    String audio
            ) {}
        }
    }
    public record Def(
            List<Object> sseq
    ){}
}


