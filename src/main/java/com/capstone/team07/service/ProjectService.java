package com.capstone.team07.service;

import com.capstone.team07.domain.Project;
import com.capstone.team07.dto.project.ProjectRequestDto;
import com.capstone.team07.dto.project.ProjectResponseDto;
import com.capstone.team07.repository.ProjectRepository;
import com.capstone.team07.repository.SentenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final SentenceRepository sentenceRepository;

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

    @Transactional
    public List<ProjectResponseDto.ProjectDto> getProjects() {
        List<Project> projects = projectRepository.findAll();

        List<ProjectResponseDto.ProjectDto> projectDtoList = projects.stream()
                .map(project -> ProjectResponseDto.ProjectDto.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .color(project.getColor())
                        .description(project.getDescription())
                        .isScriptSaved(sentenceRepository.existsByProjectId(project.getId()))
                        .build())
                .collect(Collectors.toList());

        return projectDtoList;
    }
}
