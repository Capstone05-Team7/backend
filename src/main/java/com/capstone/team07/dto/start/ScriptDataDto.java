package com.capstone.team07.dto.start;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScriptDataDto {
    @JsonProperty("id")
    private String sentenceId;

    @JsonProperty("text")
    private String sentenceContent;
}
