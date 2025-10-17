package com.capstone.team07.dto.script;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ScriptResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScriptRegisterDto{
        List<SentenceInfo> scripts;

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SentenceInfo {
            Long sentenceId;
            String sentenceContent;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScriptGetResponseDto{
        Long projectId;
        List<SentenceInfo> scripts;

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SentenceInfo {
            Long sentenceId;
            String sentenceContent;
        }
    }
}
