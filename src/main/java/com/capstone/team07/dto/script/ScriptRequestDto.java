package com.capstone.team07.dto.script;

import lombok.Getter;


public class ScriptRequestDto {

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
