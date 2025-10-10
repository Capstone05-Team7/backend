package com.capstone.team07.controller;

import com.capstone.team07.apiPayload.ApiResponse;
import com.capstone.team07.dto.project.ProjectRequestDto;
import com.capstone.team07.dto.project.ProjectResponseDto;
import com.capstone.team07.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping()
    @Operation(summary = "프로젝트 등록")
    public ApiResponse<ProjectResponseDto.ProjectRegisterDto> registerProject(@RequestBody ProjectRequestDto.ProjectRegisterDto dto) {
        return ApiResponse.onSuccess(projectService.registerProject(dto));
    }
}
