package com.capstone.team07.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProjectResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectRegisterDto{
        String name;
        String description;
        String color;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectDto{
        Long id;
        String name;
        String description;
        String color;
        Boolean isScriptSaved;
    }
}
