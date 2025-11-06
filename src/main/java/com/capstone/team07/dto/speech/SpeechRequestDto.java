package com.capstone.team07.dto.speech;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SpeechRequestDto {
    private Long projectNumber;
    private String input;
}
