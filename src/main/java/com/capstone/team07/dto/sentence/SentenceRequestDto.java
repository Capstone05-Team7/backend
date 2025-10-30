package com.capstone.team07.dto.sentence;

import lombok.Getter;


public class SentenceRequestDto {

    @Getter
    public static class ScriptRegisterDto{
        Long projectId;
        String script;
    }

    @Getter
    public static class ScriptGetRequestDto{
        Long projectId;
    }
}
