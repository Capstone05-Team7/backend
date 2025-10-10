package com.capstone.team07.dto.project;

import lombok.Getter;

public class ProjectRequestDto {
    @Getter
    public static class ProjectRegisterDto{
        String name;
        String description;
        String color;
    }
}
