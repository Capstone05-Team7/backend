package com.capstone.team07.service;

import com.capstone.team07.domain.Project;
import com.capstone.team07.dto.project.ProjectRequestDto;
import com.capstone.team07.dto.project.ProjectResponseDto;
import com.capstone.team07.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponseDto.ProjectRegisterDto registerProject(ProjectRequestDto.ProjectRegisterDto dto) {
        Project savedProject = projectRepository.save(Project.builder()
                .color(dto.getColor())
                .description(dto.getDescription())
                .name(dto.getName())
                .build()
        );

        return ProjectResponseDto.ProjectRegisterDto.builder()
                .color(savedProject.getColor())
                .description(savedProject.getDescription())
                .name(savedProject.getName())
                .build();
    }
}
