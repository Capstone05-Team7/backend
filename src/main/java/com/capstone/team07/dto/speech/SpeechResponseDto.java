package com.capstone.team07.dto.speech;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SpeechResponseDto {
    private String next;
    private long time;

    public static SpeechResponseDto fromentity(String next, long time){
        return SpeechResponseDto.builder()
                .next(next)
                .time(time)
                .build();
    }
}
