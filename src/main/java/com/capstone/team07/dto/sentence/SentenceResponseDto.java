package com.capstone.team07.dto.sentence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SentenceResponseDto {

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
            Long sentenceOrder;
            String sentenceFragmentContent;
            String keyword;
        }
    }
}
